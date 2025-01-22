package com.example.learnura;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ExamMockQuestionsGeneratorActivity extends AppCompatActivity {

    private TextView textViewExtractedText;
    private TextView textViewUploadedPDFName;
    private Spinner spinnerQuestions;
    private String extractedText = "";
    private String uploadedPDFName = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_mock_questions_generator);

        MaterialButton btnUploadPDF = findViewById(R.id.btnUploadPDF);
        MaterialButton btnExtractText = findViewById(R.id.btnExtractText);
        textViewUploadedPDFName = findViewById(R.id.textViewUploadedPDFName);
        textViewExtractedText = findViewById(R.id.textViewExtractedText);
        spinnerQuestions = findViewById(R.id.spinnerQuestions);
        FloatingActionButton fabGenerateQuestions = findViewById(R.id.fabGenerateQuestions);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getNumberList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuestions.setAdapter(adapter);

        ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(result.getData().getData());
                            PdfReader pdfReader = new PdfReader(inputStream);
                            extractedText = "";
                            uploadedPDFName = getFileNameFromUri(result.getData().getData().toString());
                            for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
                                extractedText += PdfTextExtractor.getTextFromPage(pdfReader, i);
                            }
                            pdfReader.close();
                            textViewUploadedPDFName.setText("Uploaded PDF: " + uploadedPDFName);
                            Toast.makeText(this, "PDF Text Extracted", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error Reading PDF", Toast.LENGTH_SHORT).show();
                            Log.e("PDF_ERROR", e.getMessage());
                        }
                    }
                }
        );

        btnUploadPDF.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            pdfPickerLauncher.launch(intent);
        });

        btnExtractText.setOnClickListener(v -> {
            if (extractedText.isEmpty()) {
                Toast.makeText(this, "No PDF Text to Extract", Toast.LENGTH_SHORT).show();
            } else {
                textViewExtractedText.setText(extractedText);
            }
        });

        fabGenerateQuestions.setOnClickListener(v -> generateMockQuestions());
    }

    private Integer[] getNumberList() {
        Integer[] numbers = new Integer[10];
        for (int i = 1; i <= 10; i++) {
            numbers[i - 1] = i;
        }
        return numbers;
    }

    private void generateMockQuestions() {
        int numberOfQuestions = (int) spinnerQuestions.getSelectedItem();
        Dialog loadingDialog = showLoadingDialog();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("Generate ").append(numberOfQuestions).append(" mock exam questions based on the following text:\n\n");
        queryBuilder.append(extractedText.isEmpty() ? "No extracted text provided." : extractedText);
        String query = queryBuilder.toString();

        GeminiResp.getResponse(new GeminiResp().getModel().startChat(), query, new ResponseCallback() {
            @Override
            public void onResponse(String response) {
                loadingDialog.dismiss();
                showQuestionsDialog(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onError(String error) {
                loadingDialog.dismiss();
                Toast.makeText(ExamMockQuestionsGeneratorActivity.this, "Error fetching questions: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Dialog showLoadingDialog() {
        Dialog loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        Glide.with(this).load(R.raw.progress_gif).into((ImageView) loadingDialog.findViewById(R.id.progress_gif));
        TextView progressText = loadingDialog.findViewById(R.id.progress_text);
        progressText.setText("Generating Mock Questions...");
        loadingDialog.show();
        return loadingDialog;
    }

    private void showQuestionsDialog(String mockQuestions) {
        new AlertDialog.Builder(this)
                .setTitle("Generated Mock Questions")
                .setMessage(mockQuestions)
                .setPositiveButton("Download", (dialog, which) -> downloadMockQuestions(mockQuestions))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void downloadMockQuestions(String mockQuestions) {
        try {
            // Get the Downloads directory
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadsDir, "MockQuestions.pdf");

            // Create the document and write content
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.add(new com.itextpdf.text.Paragraph(mockQuestions));
            document.close();

            // Notify the user
            Toast.makeText(this, "PDF Downloaded: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

            // Open the downloaded PDF
            openDownloadedPDF(file);
        } catch (Exception e) {
            Toast.makeText(this, "Error Saving PDF", Toast.LENGTH_SHORT).show();
            Log.e("PDF_DOWNLOAD_ERROR", e.getMessage());
        }
    }

    private void openDownloadedPDF(File file) {
        try {
            // Create a URI for the file
            Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);

            // Create an Intent to open the PDF
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Start the activity to show all apps that can open the PDF
            startActivity(Intent.createChooser(intent, "Open PDF with"));
        } catch (Exception e) {
            Toast.makeText(this, "No application available to open PDF", Toast.LENGTH_SHORT).show();
            Log.e("PDF_OPEN_ERROR", e.getMessage());
        }
    }



    private String getFileNameFromUri(String uri) {
        String[] segments = uri.split("/");
        return segments[segments.length - 1];
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExamMockQuestionsGeneratorActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.example.learnura;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class TextScannerActivity extends AppCompatActivity {

    private TextRecognizer recognizer;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap imageBitmap = null;

    private ImageView imageView;
    private TextView textView;
    private Button captureImageBtn;
    private Button detectTextImageBtn;

    private Button Copy;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_scanner);

        // Initialize views
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        captureImageBtn = findViewById(R.id.captureImage);
        detectTextImageBtn = findViewById(R.id.detectTextImageBtn);

        Copy = findViewById(R.id.copyTextBtn);

        // Initialize recognizer
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String detectedText = textView.getText().toString();
                if (!detectedText.isEmpty()){
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Detected Text", detectedText);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(TextScannerActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(TextScannerActivity.this, "No text to copy", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listeners
        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
                textView.setText("");
            }
        });

        detectTextImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processImage();
            }
        });
    }

    private void takeImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Toast.makeText(this, "Error opening camera", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data != null ? data.getExtras() : null;
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    imageView.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    private void processImage() {
        if (imageBitmap != null) {
            InputImage image = InputImage.fromBitmap(imageBitmap, 0);

            recognizer.process(image)
                    .addOnSuccessListener(visionText ->
                            textView.setText(visionText.getText())
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to detect text", Toast.LENGTH_SHORT).show()
                    );
        } else {
            Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show();
        }
    }
}
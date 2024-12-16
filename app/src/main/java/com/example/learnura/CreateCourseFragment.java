package com.example.learnura;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateCourseFragment extends Fragment {

    private EditText courseNameEditText;
    private RadioGroup categoryRadioGroup;
    private Button selectFileButton, uploadButton;
    private TextView selectedFileTextView;

    private Uri fileUri; // To store selected file URI
    private static final int FILE_SELECT_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_course, container, false);

        // Initialize UI elements
        courseNameEditText = view.findViewById(R.id.courseNameEditText);
        categoryRadioGroup = view.findViewById(R.id.categoryRadioGroup);
        selectFileButton = view.findViewById(R.id.selectFileButton);
        uploadButton = view.findViewById(R.id.uploadButton);
        selectedFileTextView = view.findViewById(R.id.selectedFileTextView);

        // Set listeners
        selectFileButton.setOnClickListener(v -> selectFile());
        uploadButton.setOnClickListener(v -> uploadCourse());

        return view;
    }

    // Method to open file picker
    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*"); // Only allow video files
        startActivityForResult(Intent.createChooser(intent, "Select Video File"), FILE_SELECT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK && data != null) {
            fileUri = data.getData();
            String fileName = fileUri.getLastPathSegment(); // Get file name
            selectedFileTextView.setText(fileName);
        }
    }

    // Method to upload course
    private void uploadCourse() {
        String courseName = courseNameEditText.getText().toString();
        int selectedCategoryId = categoryRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedCategoryButton = getView().findViewById(selectedCategoryId);
        String category = selectedCategoryButton.getText().toString();

        if (courseName.isEmpty() || fileUri == null || selectedCategoryId == -1) {
            Toast.makeText(getContext(), "Please enter a course name, select a video file, and select a category.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File file = new File(FileUtils.getPath(getContext(), fileUri)); // Use a utility to get the file path
            RequestBody requestBody = RequestBody.create(MediaType.parse("video/*"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), courseName);
            RequestBody uploadedByPart = RequestBody.create(MediaType.parse("text/plain"), "teacher@example.com"); // Replace with dynamic email
            RequestBody categoryPart = RequestBody.create(MediaType.parse("text/plain"), category);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://c036-2405-201-e009-3299-3d52-aef2-c8e8-fdbe.ngrok-free.app/learnura_api/") // Replace with your Ngrok URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CourseApi courseApi = retrofit.create(CourseApi.class);
            Call<ResponseBody> call = courseApi.uploadCourse(filePart, namePart, uploadedByPart, categoryPart);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Course uploaded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

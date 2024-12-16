package com.example.learnura;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAppUpdateFragment extends Fragment {

    private EditText appNameEditText, versionNumberEditText;
    private ImageView appImageView;
    private Button uploadButton;
    private Uri imageUri;
    private static final int IMAGE_PICK_CODE = 1000;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_app_update, container, false);

        appNameEditText = view.findViewById(R.id.update_name_editText);
        versionNumberEditText = view.findViewById(R.id.update_version_edit_text);
        appImageView = view.findViewById(R.id.choose_image);
        uploadButton = view.findViewById(R.id.add_update_btn);

        // Handle image selection
        appImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        // Handle upload button click
        uploadButton.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadAppUpdate();
            } else {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            appImageView.setImageURI(imageUri);
        }
    }

    private void uploadAppUpdate() {
        String appName = appNameEditText.getText().toString().trim();
        String versionNumber = versionNumberEditText.getText().toString().trim();

        if (appName.isEmpty() || versionNumber.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(FileUtils.getPath(getContext(), imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part imageBody = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RequestBody appNameBody = RequestBody.create(MediaType.parse("text/plain"), appName);
        RequestBody versionNumberBody = RequestBody.create(MediaType.parse("text/plain"), versionNumber);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.uploadAppUpdate(appNameBody, versionNumberBody, imageBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "App update uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Log.d("OnFailure", "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Failed to upload app update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

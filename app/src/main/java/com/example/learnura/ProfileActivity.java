package com.example.learnura;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private ImageView ivProfileImage, ivEditProfile;
    private RecyclerView rvAvatars;
    private Button btnSave;

    ImageView back;
    private int selectedAvatarResId = -1; // Default invalid value
    private UserProfileDatabase db;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#813FF1"));


        ivProfileImage = findViewById(R.id.iv_profile_image);
        rvAvatars = findViewById(R.id.rv_avatars);
        btnSave = findViewById(R.id.btn_save);
        back = findViewById(R.id.back_tv);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AuraActivity.class);
                startActivity(intent);
            }
        });

        db = Room.databaseBuilder(getApplicationContext(), UserProfileDatabase.class, "user_profile_db").build();
        sharedPreferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);

        // Load saved profile image
        int savedAvatarResId = sharedPreferences.getInt("profile_image", -1);
        if (savedAvatarResId != -1) {
            ivProfileImage.setImageResource(savedAvatarResId);
        }

        List<Avatar> avatars = Arrays.asList(
                new Avatar(R.drawable.avatar1, "Little Hero"),
                new Avatar(R.drawable.avatar2, "Speedster X"),
                new Avatar(R.drawable.avatar3, "Care Expert"),
                new Avatar(R.drawable.avatar4, "Radiant Star"),
                new Avatar(R.drawable.avatar5, "Beats Maestro"),
                new Avatar(R.drawable.avatar6, "Corporate Titan"),
                new Avatar(R.drawable.avatar7, "Melody Maker"),
                new Avatar(R.drawable.avatar8, "Healing Hands"),
                new Avatar(R.drawable.avatar9, "Supermom")
        );

        AvatarAdapter adapter = new AvatarAdapter(this, avatars, avatar -> {
            selectedAvatarResId = avatar.getImageResId();
            showConfirmationDialog();
        });
        rvAvatars.setLayoutManager(new GridLayoutManager(this, 3));
        rvAvatars.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveProfileImage());
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Set this avatar as your profile image?")
                .setPositiveButton("Yes", (dialog, which) -> ivProfileImage.setImageResource(selectedAvatarResId))
                .setNegativeButton("No", null)
                .show();
    }

    private void saveProfileImage() {
        if (selectedAvatarResId != -1) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("profile_image", selectedAvatarResId);
            editor.apply();

            // Save to Room Database
            new Thread(() -> {
                db.userProfileDao().insert(new UserProfile(selectedAvatarResId));
                runOnUiThread(() -> Toast.makeText(this, "Profile image saved", Toast.LENGTH_SHORT).show());
            }).start();
        } else {
            Toast.makeText(this, "No avatar selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this,AuraActivity.class);
        startActivity(intent);
        finish();
    }
}

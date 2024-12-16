package com.example.learnura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class AdminActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ImageView back_admin;

        back_admin = findViewById(R.id.back_admin_detail);

        back_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });

        // Get the setting type passed from AccountActivity
        String admin_settingType = getIntent().getStringExtra("ADMIN_SETTING_TYPE");

        // Load the corresponding fragment based on the setting type
        if (admin_settingType != null) {
            Fragment fragment = null;
            switch (admin_settingType) {
                case "one_app_blog":
                    fragment = new AddBlogFragment();
                    break;
                case "two_app_update":
                    fragment = new AddAppUpdateFragment();
                    break;
                case "three_teacher_review":
                    fragment = new TeachersReviewFragment();
                    break;
                case "four_student_review":
                    fragment = new StudentReviewsFragment();
                    break;


            }
            // Replace the fragment in FrameLayout
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.admin_fragment_container, fragment)
                        .commit();
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.example.learnura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class TutorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);

        ImageView back_tutor;

        back_tutor = findViewById(R.id.back_tutor_detail);

        back_tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorActivity.this, TeachersMainActivity.class);
                startActivity(intent);
            }
        });

        // Get the setting type passed from AccountActivity
        String tutor_settingType = getIntent().getStringExtra("TUTOR_SETTING_TYPE");

        // Load the corresponding fragment based on the setting type
        if (tutor_settingType != null) {
            Fragment fragment = null;
            switch (tutor_settingType) {
                case "one_add_course":
                    fragment = new CreateCourseFragment();
                    break;
                case "two_add_challenge":
                    fragment = new AddChallengesFragment();
                    break;
                case "three_course_detail":
                    fragment = new CourseDetailsFragment();
                    break;
                case "four_student_image":
                    fragment = new StudentReviewsFragment();
                    break;


            }
            // Replace the fragment in FrameLayout
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tutor_fragment_container, fragment)
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
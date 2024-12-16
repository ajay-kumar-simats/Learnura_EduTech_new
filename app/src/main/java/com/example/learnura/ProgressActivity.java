package com.example.learnura;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class ProgressActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String KEY_FIRST_LAUNCH = "isFirstLaunch";
    private MotionLayout motionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#16161D"));
        }

        motionLayout = findViewById(R.id.motionLayout);


        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true);


        if (isFirstLaunch) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_FIRST_LAUNCH, false);
            editor.apply();
        }


        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {

                if (currentId == R.id.end) {
                    if (isFirstLaunch) {
                        startActivity(new Intent(ProgressActivity.this, OnboardingActivity.class));
                    } else {
                        startActivity(new Intent(ProgressActivity.this, LoginActivity.class));
                    }
                    finish();
                }
            }

            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {}

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {}

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {}
        });
    }
}

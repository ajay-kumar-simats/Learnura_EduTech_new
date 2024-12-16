package com.example.learnura;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext;
    private TextView txtSkip;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        txtSkip = findViewById(R.id.txtSkip);

        viewPager.setAdapter(new OnboardingAdapter());

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < 2) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {

                markOnboardingSeen();
                startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
                finish();
            }
        });


        txtSkip.setOnClickListener(v -> {
            markOnboardingSeen();
            startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
            finish();
        });
    }


    private void markOnboardingSeen() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isFirstLaunch", false); // Mark first launch as false
        editor.apply();
    }
}

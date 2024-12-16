package com.example.learnura;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;

public class LearningCurveActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;

    TextView back_curve;

    LinearLayout home, achievements, aura, account, l_curve;



    private LineChart lineChart;
    private TextView correctAnswersTextView, totalQuestionsTextView, difficultyLevelTextView;
    private int correctAnswers, totalQuestions, totalTimeSpent;
    private String difficultyLevel;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_curve);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);

        back_curve = findViewById(R.id.back_l_curve);
        home = findViewById(R.id.home);
        achievements = findViewById(R.id.achievements);
        aura = findViewById(R.id.aura);
        account = findViewById(R.id.account);
        l_curve = findViewById(R.id.learning_curve);

        back_curve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearningCurveActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        // Drawer menu setup
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(LearningCurveActivity.this, MainActivity.class);
            }
        });
        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(LearningCurveActivity.this, AchievementsActivity.class);
            }
        });
        aura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(LearningCurveActivity.this, AuraActivity.class);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(LearningCurveActivity.this, AccountActivity.class);
            }
        });
        l_curve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });


        // Initialize views
        lineChart = findViewById(R.id.lineChart);
        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        totalQuestionsTextView = findViewById(R.id.totalQuestionsTextView);
        difficultyLevelTextView = findViewById(R.id.difficultyLevelTextView);

        // Get data passed from QuizActivity
        correctAnswers = getIntent().getIntExtra("correctAnswers", 0);
        totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        totalTimeSpent = getIntent().getIntExtra("totalTimeSpent", 0);
        difficultyLevel = getIntent().getStringExtra("difficultyLevel");

        // Display data in TextViews
        correctAnswersTextView.setText("Correct Answers: " + correctAnswers);
        totalQuestionsTextView.setText("Total Questions: " + totalQuestions);
        difficultyLevelTextView.setText("Difficulty Level: " + difficultyLevel);

        // Create data entries for the learning curve (for example, plotting time vs score)
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, correctAnswers)); // Day 1
        entries.add(new Entry(2, correctAnswers + 2)); // Day 2
        entries.add(new Entry(3, correctAnswers + 3)); // Day 3
        entries.add(new Entry(4, correctAnswers + 4)); // Day 4

        // Create a LineDataSet for the learning curve
        LineDataSet dataSet = new LineDataSet(entries, "Learning Curve");
        dataSet.setColor(getResources().getColor(R.color.primary));
        dataSet.setValueTextColor(getResources().getColor(R.color.primary_dark));

        // Create a LineData object
        LineData lineData = new LineData(dataSet);

        // Set the data to the line chart
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh the chart
    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LearningCurveActivity.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}

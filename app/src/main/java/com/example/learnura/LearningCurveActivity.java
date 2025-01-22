package com.example.learnura;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class LearningCurveActivity extends AppCompatActivity {

    private LineChart lineChart; // MPAndroidChart LineChart
    private GraphView usageGraph; // GraphView for app usage
    private FloatingActionButton btnGetSuggestions ,btnscanner; // Button for Gemini API suggestions

    private TextView back_l_c;

    ImageView menu;

    DrawerLayout drawerLayout;

    LinearLayout home, achievements, aura, account,l_curve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_curve);

        // Initialize views
        lineChart = findViewById(R.id.lineChart);
        usageGraph = findViewById(R.id.usageGraph);
        btnGetSuggestions = findViewById(R.id.btnGetSuggestions);
        btnscanner = findViewById(R.id.text_scanner_btn);
        back_l_c = findViewById(R.id.back_l_curve);
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        achievements = findViewById(R.id.achievements);
        aura = findViewById(R.id.aura);
        account = findViewById(R.id.account);
        l_curve = findViewById(R.id.learning_curve);

        menu.setOnClickListener(view -> openDrawer(drawerLayout));

        home.setOnClickListener(view -> redirectActivity(LearningCurveActivity.this, MainActivity.class));

        achievements.setOnClickListener(view -> redirectActivity(LearningCurveActivity.this, AchievementsActivity.class));

        aura.setOnClickListener(view -> redirectActivity(LearningCurveActivity.this, AuraActivity.class));

        account.setOnClickListener(view -> redirectActivity(LearningCurveActivity.this, AccountActivity.class));

        l_curve.setOnClickListener(view -> recreate());


        back_l_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearningCurveActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Retrieve and plot quiz and app usage data
        plotQuizGraph();
        requestUsageAccess(); // Ensure permission before fetching usage data

        // Set up button click to fetch suggestions
        btnGetSuggestions.setOnClickListener(v -> fetchSuggestions());
        btnscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearningCurveActivity.this,TextScannerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void plotQuizGraph() {
        SharedPreferences sharedPreferences = getSharedPreferences("QuizData", MODE_PRIVATE);
        String quizDataJson = sharedPreferences.getString("allQuizData", "[]");

        try {
            JSONArray quizDataArray = new JSONArray(quizDataJson);
            ArrayList<Entry> scoreEntries = new ArrayList<>();
            ArrayList<String> months = new ArrayList<>();

            for (int i = 0; i < quizDataArray.length(); i++) {
                JSONObject quizData = quizDataArray.getJSONObject(i);
                int score = quizData.getInt("score");
                String month = quizData.getString("month");

                // Add data for the graph
                scoreEntries.add(new Entry(i, score)); // X = quiz number, Y = score
                months.add(month);
            }

            // Create dataset for score
            LineDataSet dataSet = new LineDataSet(scoreEntries, "User Performance");
            dataSet.setColor(Color.BLUE);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setLineWidth(2f);

            // Create LineData and set it to the chart
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);

            // Customize chart
            lineChart.getDescription().setEnabled(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.setDrawGridBackground(false);

            // Set the X-axis labels (months)
            lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
            lineChart.invalidate(); // Refresh the chart to display the data

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestUsageAccess() {
        if (!hasUsageAccess()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        } else {
            plotUsageGraph();
        }
    }

    private boolean hasUsageAccess() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void plotUsageGraph() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        // Get current app package name
        String currentPackageName = getPackageName();

        // Define time range (past 7 days)
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_MONTH, -7); // Past 7 days
        long startTime = calendar.getTimeInMillis();

        // Fetch usage stats for the specified time range
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        int dayIndex = 1;

        if (usageStatsList != null) {
            for (UsageStats usageStats : usageStatsList) {
                if (usageStats.getPackageName().equals(currentPackageName)) {
                    long timeSpent = usageStats.getTotalTimeInForeground() / 1000 / 60; // Time in minutes

                    // Add data points for the bar graph
                    series.appendData(new DataPoint(dayIndex, timeSpent), true, 7);
                    dayIndex++;
                }
            }
        }

        // Configure the bar graph
        usageGraph.removeAllSeries();
        usageGraph.addSeries(series);
        series.setColor(R.color.bar_graph); // Change to Color.GREEN for green bars
        series.setSpacing(20); // Adjust for thinner bars

        usageGraph.getViewport().setScalable(true);
        usageGraph.getViewport().setScalableY(true);
        usageGraph.getViewport().setXAxisBoundsManual(true);
        usageGraph.getViewport().setYAxisBoundsManual(true);

        // Set bounds for X and Y axes
        usageGraph.getViewport().setMinX(1);
        usageGraph.getViewport().setMaxX(7); // Past 7 days
        usageGraph.getViewport().setMinY(0);
        usageGraph.getViewport().setMaxY(100); // Example max time in minutes

        // Configure grid label renderer for X and Y axes
        usageGraph.getGridLabelRenderer().setVerticalAxisTitle("Time Spent (minutes)");
        usageGraph.getGridLabelRenderer().setHorizontalAxisTitle("Days");
        usageGraph.setTitle("Time Spent on Learnura");
        usageGraph.setTitleColor(Color.BLACK);
    }

    private Dialog showLoadingDialog() {
        Dialog loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);

        ImageView gifView = loadingDialog.findViewById(R.id.progress_gif);
        Glide.with(this).load(R.raw.progress_gif).into(gifView); // Replace with your GIF file name in `res/drawable`.

        TextView progressText = loadingDialog.findViewById(R.id.progress_text);
        progressText.setText("Aura AI is analyzing your performance");

        loadingDialog.show();
        return loadingDialog;
    }


    private void fetchSuggestions() {
        Dialog loadingDialog = showLoadingDialog();
        // Extract quiz graph data
        ArrayList<String> quizData = new ArrayList<>();
        if (lineChart.getData() != null && lineChart.getData().getDataSets() != null) {
            LineDataSet dataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            for (int i = 0; i < dataSet.getEntryCount(); i++) {
                Entry entry = dataSet.getEntryForIndex(i);
                quizData.add("Quiz " + (i + 1) + ": Score = " + entry.getY());
            }
        }
        // Extract usage graph data
        ArrayList<String> usageData = new ArrayList<>();
        BarGraphSeries<DataPoint> series = (BarGraphSeries<DataPoint>) usageGraph.getSeries().get(0);
        for (Iterator<DataPoint> it = series.getValues(1, 7); it.hasNext(); ) {
            DataPoint point = it.next();
            usageData.add("Day " + (int) point.getX() + ": Time Spent = " + point.getY() + " minutes");
        }
        // Build the query string
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("Analyze the following data and provide learning suggestions:\n\n");
        queryBuilder.append("Quiz Data:\n");
        for (String quizEntry : quizData) {
            queryBuilder.append(quizEntry).append("\n");
        }
        queryBuilder.append("\nUsage Data:\n");
        for (String usageEntry : usageData) {
            queryBuilder.append(usageEntry).append("\n");
        }

        String query = queryBuilder.toString();

        // Call Gemini API for suggestions
        GeminiResp.getResponse(new GeminiResp().getModel().startChat(), query, new ResponseCallback() {
            @Override
            public void onResponse(String response) {
                // Display suggestions in a dialog
                new AlertDialog.Builder(LearningCurveActivity.this)
                        .setTitle("Learning Suggestions")
                        .setMessage(response)
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            // Ensure the loading dialog is dismissed when clicking OK
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                        })

                        .show();
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(LearningCurveActivity.this, "Error fetching suggestions", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (hasUsageAccess()) {
            plotUsageGraph();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LearningCurveActivity.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
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
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}

package com.example.learnura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Map;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {


    AnyChartView anyChartView;
    String[] categories_admin = {"Current Users","Users Logged out"};
    int[] values_admin = {80,20};

    TextView logout_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        anyChartView = findViewById(R.id.anyChartView_admin);
        logout_a = findViewById(R.id.admin_logout_txt);

        setupChartViewAdmin();



        TextView blog_txt = findViewById(R.id.add_blogs);
        TextView add_app_update_txt = findViewById(R.id.add_updates);
        TextView teacher_review_txt = findViewById(R.id.teacher_review);
        TextView student_review_txt = findViewById(R.id.student_reviews_admin);


        // Initialize ImageView arrows for each option
        ImageView blog_img = findViewById(R.id.one_admin);
        ImageView update_img = findViewById(R.id.two_admin);
        ImageView teacher_review_image = findViewById(R.id.three_admin);
        ImageView student_review_image_admin = findViewById(R.id.four_admin);


        logout_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this,AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for each arrow to open details
        blog_img .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminActivity("one_app_blog");
            }
        });
        update_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminActivity("two_app_update");
            }
        });
        teacher_review_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminActivity("three_teacher_review");
            }
        });
        student_review_image_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminActivity("four_student_review");
            }
        });

    }

    private void setupChartViewAdmin() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for(int i=0; i<categories_admin.length; i++){
            dataEntries.add(new ValueDataEntry(categories_admin[i],values_admin[i]));
        }
        pie.data(dataEntries);
        pie.title("Students Data");
        anyChartView.setChart(pie);
    }

    private void openAdminActivity(String adminSettingsType) {
        Intent intent = new Intent(AdminMainActivity.this, AdminActivity.class);
        intent.putExtra("ADMIN_SETTING_TYPE", adminSettingsType);
        startActivity(intent);
    }
}
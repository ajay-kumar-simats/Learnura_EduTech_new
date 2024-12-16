package com.example.learnura;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TeachersMainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    AnyChartView anyChartView;
    String[] categories = {"Active Learners", "Currently Enrolled"};
    int[] values = {20, 80};

    private RecyclerView list_of_courses;
    private TeacherCourseAdapter teacherCourseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_main);

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize chart and UI components
        anyChartView = findViewById(R.id.anyChartView);
        setupChartView();

        // Initialize TextViews for the options
        TextView add_course = findViewById(R.id.create_courses);
        TextView add_challenge = findViewById(R.id.add_challenges);
        TextView course_detail = findViewById(R.id.course_details);
        TextView review = findViewById(R.id.student_reviews);

        // Initialize ImageView arrows for each option
        ImageView course = findViewById(R.id.one);
        ImageView challenge = findViewById(R.id.two);
        ImageView detail = findViewById(R.id.three);
        ImageView review_image = findViewById(R.id.four);

        // Set OnClickListener for each arrow to open details
        course.setOnClickListener(v -> openTutorActivity("one_add_course"));
        challenge.setOnClickListener(v -> openTutorActivity("two_add_challenge"));
        detail.setOnClickListener(v -> openTutorActivity("three_course_detail"));
        review_image.setOnClickListener(v -> openTutorActivity("four_student_image"));

        // Set up RecyclerView for courses created by the teacher
        list_of_courses = findViewById(R.id.tutor_courses_created_recycler_view);
        list_of_courses.setHasFixedSize(true);
        list_of_courses.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));

        // Get the current teacher's email from FirebaseAuth
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String teacherEmail = currentUser.getEmail();
            if (teacherEmail != null && !teacherEmail.isEmpty()) {
                fetchCourses(teacherEmail);
            } else {
                Toast.makeText(this, "Error retrieving email!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }

        // Customize status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }
    }

    private void fetchCourses(String teacherEmail) {
        String url = "https://c036-2405-201-e009-3299-3d52-aef2-c8e8-fdbe.ngrok-free.app/learnura_api/fetch_courses.php?uploaded_by=" + teacherEmail;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");

                        if ("success".equals(status)) {
                            JSONArray coursesArray = jsonResponse.getJSONArray("courses");
                            List<Teacher_Course> fetchedCourses = new ArrayList<>();

                            for (int i = 0; i < coursesArray.length(); i++) {
                                JSONObject courseObject = coursesArray.getJSONObject(i);

                                String courseId = courseObject.getString("course_id");
                                String courseName = courseObject.getString("course_name");
                                String filePath = courseObject.getString("file_path");

                                // Determine the thumbnail based on the course name
                                int thumbnailResId = getThumbnailBasedOnCourseName(courseName);

                                // Add to the course list
                                fetchedCourses.add(new Teacher_Course(courseId, courseName, filePath, thumbnailResId));
                            }

                            updateRecyclerView(fetchedCourses);
                        } else {
                            Toast.makeText(TeachersMainActivity.this, "No courses found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TeachersMainActivity.this, "Error parsing response!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(TeachersMainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private int getThumbnailBasedOnCourseName(String courseName) {
        // Match course name to a specific thumbnail
        if (courseName.toLowerCase().contains("arduino")) {
            return R.drawable.arduino; // Replace with actual drawable resource
        } else if (courseName.toLowerCase().contains("iot")) {
            return R.drawable.iot; // Replace with actual drawable resource
        } else if (courseName.toLowerCase().contains("ml")) {
            return R.drawable.machine_learning; // Replace with actual drawable resource
        } else {
            return R.drawable.artificial_intelligence; // Default image
        }
    }

    private void updateRecyclerView(List<Teacher_Course> fetchedCourses) {
        teacherCourseAdapter = new TeacherCourseAdapter(fetchedCourses) {
            @Override
            public void onDeleteClick(Teacher_Course course, int position) {
                // Define delete functionality here
            }
        };
        list_of_courses.setAdapter(teacherCourseAdapter);
    }

    private void setupChartView() {
        Pie pie = AnyChart.pie3d();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i = 0; i < categories.length; i++) {
            dataEntries.add(new ValueDataEntry(categories[i], values[i]));
        }
        pie.data(dataEntries);
        pie.title("Students Data");
        anyChartView.setChart(pie);
    }

    private void openTutorActivity(String tutorSettingsType) {
        Intent intent = new Intent(TeachersMainActivity.this, TutorActivity.class);
        intent.putExtra("TUTOR_SETTING_TYPE", tutorSettingsType);
        startActivity(intent);
    }
}

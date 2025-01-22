package com.example.learnura;

import static androidx.recyclerview.widget.LinearLayoutManager.*;
import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int BACK_PRESS_INTERVAL = 2000;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 200;
    private static final String TAG = "MainActivity";

    DrawerLayout drawerLayout;
    ImageView menu,micButton;
    TextView e1txt;

    ImageView blog;

    TextView usernameTextView;
    LinearLayout home, achievements, aura, account,l_curve;

    CardView MockQuestions;

    SearchView searchView;

    private FirebaseAuth auth;

    private FirebaseDatabase database;

    private DatabaseReference usersRef;

    private long backPressedTime;

    private RecyclerView arduinoRecyclerView, iotRecyclerView, programmingRecyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MockQuestions = findViewById(R.id.cardViewMockQuestions);

        blog = findViewById(R.id.blog_img);

        usernameTextView = findViewById(R.id.name_txt_main_activity);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        searchView = findViewById(R.id.searchView);
        micButton = findViewById(R.id.img_mic);
        searchView.setIconifiedByDefault(false);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        achievements = findViewById(R.id.achievements);
        aura = findViewById(R.id.aura);
        account = findViewById(R.id.account);
        l_curve = findViewById(R.id.learning_curve);
        e1txt = findViewById(R.id.EmailTextView);

        MockQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ExamMockQuestionsGeneratorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        arduinoRecyclerView = findViewById(R.id.arduinoRecyclerView);
        arduinoRecyclerView.setHasFixedSize(true);
        arduinoRecyclerView.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));

        iotRecyclerView = findViewById(R.id.InternetRecyclerView);
        iotRecyclerView.setHasFixedSize(true);
        iotRecyclerView.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));

        programmingRecyclerView = findViewById(R.id.ProgrammmingRecyclerView);
        programmingRecyclerView.setHasFixedSize(true);
        programmingRecyclerView.setLayoutManager(new LinearLayoutManager(this, HORIZONTAL, false));

        fetchCourses();

        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            usersRef.child(userId).child("username").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String username = task.getResult().getValue(String.class);
                    usernameTextView.setText(username);
                } else {
                    Toast.makeText(this, "Error fetching username.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Logged in as Guest", Toast.LENGTH_SHORT).show();

        }


        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BlogActivity.class);
                startActivity(intent);
            }
        });
        e1txt.setOnClickListener(v -> {
            String emailAddress = "ajashiatechnologies@gmail.com";
            String subject = "Feedback / Suggestion";
            String message = "Kindly write your valuable message here!";

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + emailAddress));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "No email client is installed.", Toast.LENGTH_SHORT).show();
            }
        });

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.GRAY);
        searchEditText.setHint("Search Courses");

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setColorFilter(Color.BLACK);

        ImageView searchCloseIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchCloseIcon.setColorFilter(Color.GRAY);

        menu.setOnClickListener(view -> openDrawer(drawerLayout));

        home.setOnClickListener(view -> recreate());

        achievements.setOnClickListener(view -> redirectActivity(MainActivity.this, AchievementsActivity.class));

        aura.setOnClickListener(view -> redirectActivity(MainActivity.this, AuraActivity.class));

        account.setOnClickListener(view -> redirectActivity(MainActivity.this, AccountActivity.class));

        l_curve.setOnClickListener(view -> redirectActivity(MainActivity.this, LearningCurveActivity.class));



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }
    }

    private void fetchCourses() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.getCourses().enqueue(new Callback<List<Course1>>() {
            @Override
            public void onResponse(Call<List<Course1>> call, Response<List<Course1>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Course1> courses = response.body();

                    List<Course1> arduinoCourses = new ArrayList<>();
                    List<Course1> iotCourses = new ArrayList<>();
                    List<Course1> programmingCourses = new ArrayList<>();

                    for (Course1 course : courses) {
                        Log.d(TAG, "Course ID: " + course.getCourseId() + ", Course Name: " + course.getCourseName() + ", Uploaded By: " + course.getUploadedBy() + ", Category: " + course.getCategory());
                        if (course.getCategory() != null) {
                            switch (course.getCategory()) {
                                case "Arduino":
                                    arduinoCourses.add(course);
                                    break;
                                case "IoT":
                                    iotCourses.add(course);
                                    break;
                                case "Programming":
                                    programmingCourses.add(course);
                                    break;
                                default:
                                    Log.w(TAG, "Unknown category: " + course.getCategory());
                            }
                        }
                    }

                    ArduinoAdapter arduinoAdapter = new ArduinoAdapter(arduinoCourses, MainActivity.this);
                    InternetAdapter iotAdapter = new InternetAdapter(iotCourses, MainActivity.this);
                    ProgrammingAdapter programmingAdapter = new ProgrammingAdapter(programmingCourses, MainActivity.this);



                    arduinoRecyclerView.setAdapter(arduinoAdapter);
                    iotRecyclerView.setAdapter(iotAdapter);
                    programmingRecyclerView.setAdapter(programmingAdapter);

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            arduinoAdapter.filter(newText);
                            iotAdapter.filter(newText);
                            programmingAdapter.filter(newText);
                            return true;
                        }
                    });

                    micButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.RECORD_AUDIO)) {
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setMessage("We need your permission to access the microphone for voice search.")
                                            .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_CODE))
                                            .show();
                                } else {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION_CODE);
                                }
                            } else {
                                startVoiceInput();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
                }


            }



            @Override
            public void onFailure(Call<List<Course1>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Your device doesn't support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                searchView.setQuery(result.get(0), true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVoiceInput();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + BACK_PRESS_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            backPressedTime = System.currentTimeMillis();
        }
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
}

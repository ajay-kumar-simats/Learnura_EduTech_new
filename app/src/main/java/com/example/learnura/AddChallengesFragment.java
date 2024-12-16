package com.example.learnura;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChallengesFragment extends Fragment {

    private Spinner difficultySpinner, courseSpinner;
    private EditText questionEditText, option1EditText, option2EditText, option3EditText, option4EditText, correctAnswerEditText;
    private Button submitButton;

    List<Course1> courses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_challenges, container, false);

        // Change the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }

        // Initialize UI components
        difficultySpinner = view.findViewById(R.id.difficultySpinner);
        courseSpinner = view.findViewById(R.id.courseSpinner); // New course spinner
        questionEditText = view.findViewById(R.id.questionEditText);
        option1EditText = view.findViewById(R.id.option1EditText);
        option2EditText = view.findViewById(R.id.option2EditText);
        option3EditText = view.findViewById(R.id.option3EditText);
        option4EditText = view.findViewById(R.id.option4EditText);
        correctAnswerEditText = view.findViewById(R.id.correctAnswerEditText);
        submitButton = view.findViewById(R.id.submitButton);

        // Create a list of difficulty levels
        String[] difficultyLevels = {"Easy", "Medium", "Hard"};

        // Create an ArrayAdapter using the difficultyLevels array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, difficultyLevels);

        // Specify the layout for the dropdown list
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        difficultySpinner.setAdapter(adapter);

        // Fetch and populate course spinner
        fetchCourses();

        // Set the submit button click listener
        submitButton.setOnClickListener(v -> handleSubmitButtonClick());

        return view;
    }

    private void fetchCourses() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Course1>> call = apiService.getCourses();

        call.enqueue(new Callback<List<Course1>>() {
            @Override
            public void onResponse(@NonNull Call<List<Course1>> call, @NonNull Response<List<Course1>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courses = response.body();

                    String[] itemNames = new String[courses.size()];
                    for (int i = 0; i < courses.size(); i++) {
                        itemNames[i] = courses.get(i).getCourseName();  // Extract name from each Item object
                    }
                    populateCourseSpinner(itemNames);

                } else {
                    Toast.makeText(getContext(), "Failed to fetch courses", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Course1>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateCourseSpinner(String[] courses) {


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
    }

    private void handleSubmitButtonClick() {
        // Check if spinner selection is valid
        String difficulty = difficultySpinner.getSelectedItem() != null ? difficultySpinner.getSelectedItem().toString() : "";
        String question = questionEditText.getText().toString().trim();
        String option1 = option1EditText.getText().toString().trim();
        String option2 = option2EditText.getText().toString().trim();
        String option3 = option3EditText.getText().toString().trim();
        String option4 = option4EditText.getText().toString().trim();
        String correctAnswer = correctAnswerEditText.getText().toString().trim();

        // Get selected course
        int selectedItem = courseSpinner.getSelectedItemPosition();
        int courseId = courses.get(selectedItem).getCourseId();

        // Validate fields
        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || correctAnswer.isEmpty() || difficulty.isEmpty() || courseId == -1) {
            Toast.makeText(getContext(), "Please fill in all fields and select a course", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the challenge object
        Challenge challenge = new Challenge();
        challenge.setQuestion(question);
        challenge.setOption1(option1);
        challenge.setOption2(option2);
        challenge.setOption3(option3);
        challenge.setOption4(option4);
        challenge.setCorrectAnswer(correctAnswer);
        challenge.setDifficulty_level(difficulty);
        challenge.setCourse_id(courseId);

        // Call the API service to upload the challenge
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.uploadChallenge(challenge);

        // Make the API call
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Challenge Uploaded!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(getContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to clear all fields after submission
    private void clearFields() {
        questionEditText.setText("");
        option1EditText.setText("");
        option2EditText.setText("");
        option3EditText.setText("");
        option4EditText.setText("");
        correctAnswerEditText.setText("");
        difficultySpinner.setSelection(0); // Reset spinner to first item
        courseSpinner.setSelection(0); // Reset course spinner to first item
    }
}

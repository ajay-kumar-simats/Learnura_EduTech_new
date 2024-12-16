package com.example.learnura;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {
    private TextView questionTextView, difficultyLevelTextView, timerTextView;
    private VideoView videoView;
    private Button option1Button, option2Button, option3Button, option4Button, learningCurveButton;
    private List<Challenge> challengesList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int courseId;
    private int score = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private CountDownTimer countDownTimer;
    private long timeSpent = 0;
    private long totalTimeSpent = 0;
    private long startTime;
    private String currentDifficulty = "Easy";
    private List<Challenge> easyChallenges = new ArrayList<>();
    private List<Challenge> mediumChallenges = new ArrayList<>();
    private List<Challenge> hardChallenges = new ArrayList<>();
    private final int TOTAL_QUESTIONS = 15;
    private Set<Integer> askedQuestionIds = new HashSet<>();
    private int analyzedQuestions = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        startTime = System.currentTimeMillis();

        questionTextView = findViewById(R.id.idTvPythonQuestion);
        difficultyLevelTextView = findViewById(R.id.tvDifficultyLevel);
        timerTextView = findViewById(R.id.tvTimer);
        videoView = findViewById(R.id.courseVideoView);
        option1Button = findViewById(R.id.idBtnOption1);
        option2Button = findViewById(R.id.idBtnOption2);
        option3Button = findViewById(R.id.idBtnOption3);
        option4Button = findViewById(R.id.idBtnOption4);

        courseId = getIntent().getIntExtra("course_id", -1);

        option1Button.setOnClickListener(v -> checkAnswer(option1Button.getText().toString()));
        option2Button.setOnClickListener(v -> checkAnswer(option2Button.getText().toString()));
        option3Button.setOnClickListener(v -> checkAnswer(option3Button.getText().toString()));
        option4Button.setOnClickListener(v -> checkAnswer(option4Button.getText().toString()));


        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        fetchVideoPath(courseId);
        fetchChallenges();
    }

    private void fetchVideoPath(int courseId) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.getVideoPath(courseId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String videoPath = response.body().string();
                        videoPath = videoPath.replace("{\"file_path\":\"", "").replace("\"}", "");
                        Uri videoUri = Uri.parse("https://c036-2405-201-e009-3299-3d52-aef2-c8e8-fdbe.ngrok-free.app/learnura_api/" + videoPath);
                        videoView.setVideoURI(videoUri);
                        videoView.setOnPreparedListener(mp -> {
                            mp.setOnBufferingUpdateListener((mp1, percent) -> {
                                if (percent > 5) {
                                    videoView.start();
                                }
                            });
                        });
                        videoView.seekTo(2000); // Wait for at least 2 seconds before enabling quiz
                        showStartQuizDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(QuizActivity.this, "Failed to load video", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "Failed to load video", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStartQuizDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Start Quiz?")
                .setMessage("Do you want to start the quiz after watching the video?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    fetchChallenges();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void fetchChallenges() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<Challenge>> call = apiService.getChallenges(courseId);

        call.enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Challenge challenge : response.body()) {
                        switch (challenge.getDifficulty_level().toLowerCase()) {
                            case "easy":
                                easyChallenges.add(challenge);
                                break;
                            case "medium":
                                mediumChallenges.add(challenge);
                                break;
                            case "hard":
                                hardChallenges.add(challenge);
                                break;
                        }
                    }
                    prepareQuestions();
                } else {
                    Toast.makeText(QuizActivity.this, "Failed to load challenges", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                Toast.makeText(QuizActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareQuestions() {
        challengesList.addAll(selectRandomChallenges(easyChallenges, 1));
        loadQuestion();
    }

    private List<Challenge> selectRandomChallenges(List<Challenge> challenges, int count) {
        List<Challenge> selectedChallenges = new ArrayList<>();
        if (challenges.size() >= count) {
            Collections.shuffle(challenges);
            for (Challenge challenge : challenges) {
                if (!askedQuestionIds.contains(challenge.getId()) && selectedChallenges.size() < count) {
                    selectedChallenges.add(challenge);
                    askedQuestionIds.add(challenge.getId());
                }
            }
        }
        return selectedChallenges;
    }

    private void loadQuestion() {
        if (currentQuestionIndex < challengesList.size()) {
            Challenge currentChallenge = challengesList.get(currentQuestionIndex);
            questionTextView.setText(currentChallenge.getQuestion());
            option1Button.setText(currentChallenge.getOption1());
            option2Button.setText(currentChallenge.getOption2());
            option3Button.setText(currentChallenge.getOption3());
            option4Button.setText(currentChallenge.getOption4());
            currentDifficulty = currentChallenge.getDifficulty_level();
            difficultyLevelTextView.setText("Difficulty Level: " + currentDifficulty);
            startTimer();
            totalQuestions++;
        } else if (totalQuestions >= TOTAL_QUESTIONS) {
            showFinalScore();
        } else {
            prepareQuestions();
        }
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeSpent = 30 - (millisUntilFinished / 1000);
                timerTextView.setText("Time: " + timeSpent + "s");
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                nextQuestion();
            }
        }.start();
    }

    private void checkAnswer(String selectedAnswer) {
        if (currentQuestionIndex < challengesList.size()) {
            Challenge currentChallenge = challengesList.get(currentQuestionIndex);
            if (currentChallenge.getCorrectAnswer().equals(selectedAnswer)) {
                score++;
                correctAnswers++;
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();
            }
            totalQuestions++;
            analyzedQuestions++;
            totalTimeSpent += timeSpent;
            adjustDifficultyBasedOnPerformance();
            nextQuestion();
        }
    }

    private void adjustDifficultyBasedOnPerformance() {
        if (analyzedQuestions >= 5) {
            if (correctAnswers >= 4 && timeSpent < 20) {
                currentDifficulty = "Hard";
            } else if (correctAnswers >= 3) {
                currentDifficulty = "Medium";
            } else {
                currentDifficulty = "Easy";
            }
            challengesList.addAll(selectRandomChallenges(getChallengesByDifficulty(currentDifficulty), 1));
        }
    }

    private List<Challenge> getChallengesByDifficulty(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return easyChallenges;
            case "medium":
                return mediumChallenges;
            case "hard":
                return hardChallenges;
            default:
                return new ArrayList<>();
        }
    }

    private void nextQuestion() {
        currentQuestionIndex++;
        loadQuestion();
    }

    private void showFinalScore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quiz Completed");
        builder.setMessage("Your Score: " + score + "/" + "15" +
                "\nTime Spent: " + totalTimeSpent + " seconds" +
                "\nDifficulty Level: " + currentDifficulty);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            showLearningCurve();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void showLearningCurve() {
        Intent intent = new Intent(this, LearningCurveActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("timeSpent", totalTimeSpent);
        intent.putExtra("difficulty", currentDifficulty);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

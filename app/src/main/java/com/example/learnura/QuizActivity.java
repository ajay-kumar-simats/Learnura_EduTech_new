package com.example.learnura;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.common.MediaItem;
import androidx.media3.ui.PlayerView;




import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
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
    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private Button option1Button, option2Button, option3Button, option4Button;
    private List<Challenge> challengesList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int courseId;
    private int score = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private ImageView play;
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
    private boolean quizEnabled = false;
    private boolean isPaused = false;
    private long pauseTimeRemaining = 60000;
    private List<Long> timeTakenPerQuestion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        startTime = System.currentTimeMillis();

        questionTextView = findViewById(R.id.idTvPythonQuestion);
        difficultyLevelTextView = findViewById(R.id.tvDifficultyLevel);
        timerTextView = findViewById(R.id.tvTimer);
        playerView = findViewById(R.id.courseVideoView);
        option1Button = findViewById(R.id.idBtnOption1);
        option2Button = findViewById(R.id.idBtnOption2);
        option3Button = findViewById(R.id.idBtnOption3);
        option4Button = findViewById(R.id.idBtnOption4);

        play = findViewById(R.id.play_iv);
        play.setOnClickListener(v -> showStartQuizDialog());

        courseId = getIntent().getIntExtra("course_id", -1);

        option1Button.setOnClickListener(v -> {
            if (quizEnabled) checkAnswer(option1Button.getText().toString());
        });
        option2Button.setOnClickListener(v -> {
            if (quizEnabled) checkAnswer(option2Button.getText().toString());
        });
        option3Button.setOnClickListener(v -> {
            if (quizEnabled) checkAnswer(option3Button.getText().toString());
        });
        option4Button.setOnClickListener(v -> {
            if (quizEnabled) checkAnswer(option4Button.getText().toString());
        });

        timerTextView.setText("0 seconds");
        fetchVideoPath(courseId);
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
                        Uri videoUri = Uri.parse("https://7d3a-2405-201-e009-3299-6912-5674-dc40-a7d8.ngrok-free.app/learnura_api/" + videoPath);
                        initializePlayer(videoUri);
                    } catch (Exception e) {
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

    private void initializePlayer(Uri videoUri) {
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void showStartQuizDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Start Quiz?")
                .setMessage("Do you want to start the quiz and the timer?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    startTimer();
                    quizEnabled = true;
                    fetchChallenges();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(pauseTimeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pauseTimeRemaining = millisUntilFinished;
                timeSpent = 60 - (millisUntilFinished / 1000);
                timerTextView.setText("Time: " + timeSpent + "s");
            }

            @Override
            public void onFinish() {
                if (!isFinishing()) {
                    Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                    showFinalScore();
                }
            }
        }.start();
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
        challengesList.addAll(selectRandomChallenges(easyChallenges, 5));
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
        } else {
            if (totalQuestions == TOTAL_QUESTIONS) {
                showFinalScore();
            } else {
                adjustDifficultyAndPrepareNextSet();
            }
        }
    }

    private void adjustDifficultyAndPrepareNextSet() {
        if (analyzedQuestions >= 5) {
            if (correctAnswers >= 4 && averageTimeSpent() <= 20) {
                currentDifficulty = "Hard";
            } else if (correctAnswers >= 2 && averageTimeSpent() <= 30) {
                currentDifficulty = "Medium";
            } else {
                currentDifficulty = "Easy";
            }
        }
        if (currentDifficulty.equals("Easy") && easyChallenges.size() > 0) {
            challengesList.addAll(selectRandomChallenges(easyChallenges, 5));
        } else if (currentDifficulty.equals("Medium") && mediumChallenges.size() > 0) {
            challengesList.addAll(selectRandomChallenges(mediumChallenges, 5));
        } else if (currentDifficulty.equals("Hard") && hardChallenges.size() > 0) {
            challengesList.addAll(selectRandomChallenges(hardChallenges, 5));
        }
        currentQuestionIndex = 0;
        loadQuestion();
    }

    private double averageTimeSpent() {
        if (timeTakenPerQuestion.isEmpty()) return 0;
        long totalTime = 0;
        for (Long time : timeTakenPerQuestion) {
            totalTime += time;
        }
        return (double) totalTime / timeTakenPerQuestion.size();
    }

    private void showFinalScore() {
        long timeTakenInSeconds = totalTimeSpent;
        saveQuizData(score, timeTakenInSeconds);

        SharedPreferences sharedPreferences = getSharedPreferences("QuizData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("score", score); // Store score
        editor.putLong("timeTaken", timeTakenInSeconds); // Store time
        editor.putInt("totalQuestions", TOTAL_QUESTIONS); // Store the total number of questions
        editor.apply(); // Save the data

        // Show score and time to the user
        new AlertDialog.Builder(this)
                .setTitle("Quiz Completed!")
                .setMessage("Your score is: " + score + " / " + TOTAL_QUESTIONS + "\nTime taken: " + timeTakenInSeconds + " seconds")
                .setPositiveButton("OK", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(exoPlayer!=null){
            exoPlayer.pause();
        }
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (exoPlayer!=null){
            exoPlayer.play();
        }
        if (quizEnabled && pauseTimeRemaining>0){
            startTimer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer!=null){
            exoPlayer.release();
            exoPlayer = null;
        }
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    private void checkAnswer(String selectedAnswer) {
        if (currentQuestionIndex < challengesList.size()) {
            Challenge currentChallenge = challengesList.get(currentQuestionIndex);
            if (currentChallenge.getCorrectAnswer().equals(selectedAnswer)) {
                score++;
                correctAnswers++;
            }
            totalQuestions++;
            analyzedQuestions++;
            totalTimeSpent += timeSpent;
            timeTakenPerQuestion.add(timeSpent);
            if (totalQuestions == TOTAL_QUESTIONS || pauseTimeRemaining <= 0) {
                showFinalScore();
            } else {
                nextQuestion();
            }
        }
    }

    private void nextQuestion() {
        if (countDownTimer != null) countDownTimer.cancel();
        currentQuestionIndex++;
        if (totalQuestions < TOTAL_QUESTIONS) {
            loadQuestion();
        } else {
            showFinalScore();

        }
    }

    private void saveQuizData(int score, long timeTaken) {
        SharedPreferences sharedPreferences = getSharedPreferences("QuizData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Get existing data
        String quizDataJson = sharedPreferences.getString("allQuizData", "[]");
        try {
            JSONArray quizDataArray = new JSONArray(quizDataJson);

            // Create new quiz data JSON object
            JSONObject quizData = new JSONObject();
            quizData.put("score", score);
            quizData.put("timeTaken", timeTaken);
            quizData.put("month", getCurrentMonth());  // Store the current month (you can use Calendar.getInstance() to get the current month)
            quizDataArray.put(quizData);

            // Save the updated quiz data
            editor.putString("allQuizData", quizDataArray.toString());
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentMonth() {
        // Return the current month as a string (you can customize this)
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1; // Get the current month (1-based)
        return "Month " + month;

    }
}
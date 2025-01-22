package com.example.learnura;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FeedbackFragment extends Fragment {

    private EditText editTextUsername, editTextEmail, editTextFeedback;
    private RatingBar ratingBar;
    private Button submitButton;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        // Change the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#813FF1")); // Use your desired color code
        }

        // Initialize UI elements
        editTextUsername = view.findViewById(R.id.editTextText5);
        editTextEmail = view.findViewById(R.id.editTextText7);
        editTextFeedback = view.findViewById(R.id.editTextText8);
        ratingBar = view.findViewById(R.id.ratingBar);
        submitButton = view.findViewById(R.id.button5);

        // Set click listener for the Submit Feedback button
        submitButton.setOnClickListener(v -> sendFeedback());

        return view;
    }

    private void sendFeedback() {
        // Get input values
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String feedback = editTextFeedback.getText().toString().trim();
        float rating = ratingBar.getRating();

        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || feedback.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all the fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare email content
        String subject = "Feedback from " + username;
        String message = "Name: " + username + "\n"
                + "Email: " + email + "\n"
                + "Rating: " + rating + " stars\n"
                + "Feedback: \n" + feedback;

        // Send email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ajashiatechnologies@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Feedback via"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No email clients are installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.learnura;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    TextView name, mail;
    EditText etName, etAge, etMail, etInterest;
    ImageView backEp;
    Button updateButton;

    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        name = findViewById(R.id.ep_name);
        mail = findViewById(R.id.ep_mail);
        etName = findViewById(R.id.editTextText);
        etAge = findViewById(R.id.editTextText2);
        etMail = findViewById(R.id.mail_et);
        etInterest = findViewById(R.id.editTextText4);
        backEp = findViewById(R.id.back_ep_to_settings);
        updateButton = findViewById(R.id.button3);

        // Firebase setup
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Retrieve current user details
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId != null) {
            usersRef.child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    String username = task.getResult().child("username").getValue(String.class);
                    String email = task.getResult().child("email").getValue(String.class);

                    name.setText(username);
                    mail.setText(email);
                    etName.setText(username);
                    etMail.setText(email);
                } else {
                    Toast.makeText(this, "Failed to fetch user details!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Back button action
        backEp.setOnClickListener(v -> {
            startActivity(new Intent(EditProfileActivity.this, AccountActivity.class));
            finish();
        });

        // Update button action
        updateButton.setOnClickListener(v -> {
            String updatedName = etName.getText().toString().trim();
            String updatedAge = etAge.getText().toString().trim();
            String updatedInterest = etInterest.getText().toString().trim();

            if (updatedName.isEmpty() || updatedAge.isEmpty() || updatedInterest.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userId != null) {
                usersRef.child(userId).child("username").setValue(updatedName);
                usersRef.child(userId).child("age").setValue(updatedAge);
                usersRef.child(userId).child("interest").setValue(updatedInterest).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to update profile!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Customize status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }
    }
}

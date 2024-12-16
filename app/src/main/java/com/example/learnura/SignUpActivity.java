package com.example.learnura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = database.getReference("Users");
    private EditText signupEmail;
    private EditText signupUsername;
    private Button signupButton;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.sign_up_email_edit_text);
        signupButton = findViewById(R.id.sign_up_btn);
        signupUsername = findViewById(R.id.sign_up_user_edit_text);
        passwordEditText = findViewById(R.id.sign_up_pass_edit_text);
        confirmPasswordEditText = findViewById(R.id.sign_up_confirm_pass_edit_text);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sign_up_user = signupUsername.getText().toString().trim();
                String signup_email = signupEmail.getText().toString().trim();
                String sign_up_pass = passwordEditText.getText().toString().trim();
                String signup_confirm_pass = confirmPasswordEditText.getText().toString().trim();

                if (sign_up_user.isEmpty() || signup_email.isEmpty() || sign_up_pass.isEmpty() || signup_confirm_pass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                }
                if (!sign_up_pass.equals(signup_confirm_pass)) {
                    Toast.makeText(SignUpActivity.this, "Password is not matched!", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(signup_email, sign_up_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = auth.getCurrentUser().getUid();
                                DatabaseReference userRef = usersRef.child(userId);
                                userRef.child("username").setValue(sign_up_user);
                                userRef.child("email").setValue(signup_email);
                                Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp Failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(passwordEditText, true);
                        return true;
                    }
                }
                return false;
            }
        });

        confirmPasswordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (confirmPasswordEditText.getRight() - confirmPasswordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(confirmPasswordEditText, false);
                        return true;
                    }
                }
                return false;
            }
        });

        TextView loginTextView = findViewById(R.id.login_txt);
        String fullText = "Already have an account? Login";
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 25, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginTextView.setText(spannableString);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void togglePasswordVisibility(EditText editText, boolean isPassword) {
        if (isPassword) {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.pass_eye_open, 0);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.pass_eye_close, 0);
            }
        } else {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            if (isConfirmPasswordVisible) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.pass_eye_open, 0);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.pass_eye_close, 0);
            }
        }
        editText.setSelection(editText.getText().length());
    }
}

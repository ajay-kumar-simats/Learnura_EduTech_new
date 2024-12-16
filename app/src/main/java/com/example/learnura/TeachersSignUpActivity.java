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

public class TeachersSignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth_t;
    private EditText signupEmail_t;
    private EditText signupUsername_t;
    private Button signupButton_t;
    private EditText passwordEditText_t;
    private EditText confirmPasswordEditText_t;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    TextView go_to_teachers_login;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_sign_up);

        auth_t = FirebaseAuth.getInstance();
        signupEmail_t = findViewById(R.id.teacher_mail);
        signupButton_t = findViewById(R.id.teachers_sign_up_btn);
        signupUsername_t = findViewById(R.id.teacher_username);
        // Initialize password fields
        passwordEditText_t = findViewById(R.id.teacher_pass_edit_text);
        confirmPasswordEditText_t = findViewById(R.id.teacher_pass_confirm_edit_text);



        go_to_teachers_login = findViewById(R.id.teachers_login_txt_view);

        signupButton_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t_sign_up_user = signupUsername_t.getText().toString().trim();
                String t_signup_email = signupEmail_t.getText().toString().trim();
                String t_sign_up_pass = passwordEditText_t.getText().toString().trim();
                String t_signup_confirm_pass = confirmPasswordEditText_t.getText().toString().trim();


                if(t_sign_up_user.isEmpty() || t_signup_email.isEmpty() || t_sign_up_pass.isEmpty() || t_signup_confirm_pass.isEmpty()){
                    Toast.makeText(TeachersSignUpActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
                }if(!t_sign_up_pass.equals(t_signup_confirm_pass)){
                    Toast.makeText(TeachersSignUpActivity.this, "Password is not matched!", Toast.LENGTH_SHORT).show();
                }else{
                    auth_t.createUserWithEmailAndPassword(t_signup_email,t_sign_up_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(TeachersSignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TeachersSignUpActivity.this,TeachersLoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(TeachersSignUpActivity.this, "SignUp Failed!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        // Toggle password visibility on drawable click for password field
        passwordEditText_t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText_t.getRight() - passwordEditText_t.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(passwordEditText_t, true);
                        return true;
                    }
                }
                return false;
            }
        });

        // Toggle password visibility on drawable click for confirm password field
        confirmPasswordEditText_t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (confirmPasswordEditText_t.getRight() - confirmPasswordEditText_t.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(confirmPasswordEditText_t, false);
                        return true;
                    }
                }
                return false;
            }
        });


        // Set up the "Already have an account? Login" text with color spans
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView t_loginTextView = findViewById(R.id.teachers_login_txt_view);
        String fullText = "Already have an account? Login";
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 25, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t_loginTextView.setText(spannableString);

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }

        go_to_teachers_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeachersSignUpActivity.this,TeachersLoginActivity.class);
                startActivity(intent);
            }
        });
    }
    // Method to toggle password visibility
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
        // Move cursor to the end of the text
        editText.setSelection(editText.getText().length());
    }
}
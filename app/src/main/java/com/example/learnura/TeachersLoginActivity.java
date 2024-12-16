package com.example.learnura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TeachersLoginActivity extends AppCompatActivity {

    private FirebaseAuth teacher_login_auth;
    private EditText t_loginEmail;
    private Button t_loginButton;
    private EditText t_login_passwordEditText;

    TextView back_to_stu,a_login;

    private boolean isPasswordVisible = false;
    TextView go_to_sign_up;

    TextView t_forgot;


    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_login);
        teacher_login_auth = FirebaseAuth.getInstance();
        t_loginEmail = findViewById(R.id.teacher_login_email_edit_text);
        t_login_passwordEditText = findViewById(R.id.teacher_login_pass_edit_text);
        t_loginButton = findViewById(R.id.teachers_login_button);
        t_forgot = findViewById(R.id.forgot_txt_teachers_login);

        back_to_stu = findViewById(R.id.back_to_student_login_txt);
        a_login = findViewById(R.id.admin_login_txt);
        go_to_sign_up = findViewById(R.id.teachers_sign_up_txt);

        // Set up the "New User? Create Account" text with color spans
        TextView t_signUp = findViewById(R.id.teachers_sign_up_txt);
        String fullText = "New User? Create Account";
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 10, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t_signUp.setText(spannableString);

        go_to_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeachersLoginActivity.this,TeachersSignUpActivity.class);
                startActivity(intent);
            }
        });


        back_to_stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeachersLoginActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        a_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeachersLoginActivity.this,AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        t_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Forgot password dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TeachersLoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailForgot = dialogView.findViewById(R.id.emailForgotBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user_forgot_email = emailForgot.getText().toString().trim();

                        if (TextUtils.isEmpty(user_forgot_email) || !Patterns.EMAIL_ADDRESS.matcher(user_forgot_email).matches()) {
                            Toast.makeText(TeachersLoginActivity.this, "Enter your registered mail address", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        teacher_login_auth.sendPasswordResetEmail(user_forgot_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(TeachersLoginActivity.this, "Please check your mail", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(TeachersLoginActivity.this, "Unable to send, reset failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

        t_loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_email = t_loginEmail.getText().toString().trim();
                String login_pass =  t_login_passwordEditText.getText().toString().trim();

                if (!login_email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(login_email).matches()) {
                    if (!login_pass.isEmpty()) {
                        teacher_login_auth.signInWithEmailAndPassword(login_email, login_pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(TeachersLoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(TeachersLoginActivity.this, TeachersMainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TeachersLoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(TeachersLoginActivity.this, "Password cannot be Empty", Toast.LENGTH_SHORT).show();
                    }
                } else if (login_email.isEmpty()) {
                    t_loginEmail.setError("Email cannot be empty!");
                } else {
                    t_loginEmail.setError("Please enter a valid email");
                }
            }
        });

        // Toggle password visibility on drawable click
        t_login_passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;  // Right drawable is index 2
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (t_login_passwordEditText.getRight() - t_login_passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Clicked on drawable (eye icon)
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password and change drawable to pass_eye_close
            t_login_passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            t_login_passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.pass_eye_close, 0);
        } else {
            // Show password and change drawable to pass_eye_open
            t_login_passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            t_login_passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.pass_eye_open, 0);
        }
        // Move cursor to the end of the text
        t_login_passwordEditText.setSelection(t_login_passwordEditText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
}
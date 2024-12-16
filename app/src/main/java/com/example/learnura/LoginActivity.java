package com.example.learnura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail;
    private Button loginButton;
    private EditText passwordEditText;

    TextView Skip;

    TextView t_login;


    TextView forgot;
    private boolean isPasswordVisible = false;

    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        NoInternetDialog();

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email_edit_text);
        passwordEditText = findViewById(R.id.admin_pass_edit_text);
        loginButton = findViewById(R.id.admin_login_btn);
        forgot = findViewById(R.id.forgot_txt);
        Skip = findViewById(R.id.skip_login_txt);
        t_login = findViewById(R.id.teachers_login);

        t_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,TeachersLoginActivity.class);
                startActivity(intent);
            }
        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Forgot password dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailForgot = dialogView.findViewById(R.id.emailForgotBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         String user_forgot_email = emailForgot.getText().toString().trim();

                        if (TextUtils.isEmpty(user_forgot_email) || !Patterns.EMAIL_ADDRESS.matcher(user_forgot_email).matches()) {
                            Toast.makeText(LoginActivity.this, "Enter your registered mail address", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(user_forgot_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Please check your mail", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Unable to send, reset failed!", Toast.LENGTH_SHORT).show();
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_email = loginEmail.getText().toString().trim();
                String login_pass = passwordEditText.getText().toString().trim();

                if (!login_email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(login_email).matches()) {
                    if (!login_pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(login_email, login_pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, "Password cannot be Empty", Toast.LENGTH_SHORT).show();
                    }
                } else if (login_email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty!");
                } else {
                    loginEmail.setError("Please enter a valid email");
                }
            }
        });

        // Toggle password visibility on drawable click
        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;  // Right drawable is index 2
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Clicked on drawable (eye icon)
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        // Set up the "New User? Create Account" text with color spans
        TextView signUp = findViewById(R.id.sign_up_txt);
        String fullText = "New User? Create Account";
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 10, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUp.setText(spannableString);

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }

        // Handle sign-up text click to move to SignUpActivity
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void NoInternetDialog() {
        NoInternetDialogSignal.Builder builder;
        builder = new NoInternetDialogSignal.Builder(
                this,
                getLifecycle()
        );

        DialogPropertiesSignal properties = builder.getDialogProperties();

        properties.setConnectionCallback(new ConnectionCallback() {
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {

            }
        });

        properties.setCancelable(false);
        properties.setNoInternetConnectionTitle("No Internet");
        properties.setNoInternetConnectionMessage("Check your Internet connection and try again");

        properties.setShowInternetOnButtons(true);
        properties.setPleaseTurnOnText("Please turn on");
        properties.setWifiOnButtonText("Wifi");
        properties.setMobileDataOnButtonText("Mobile data");

        properties.setOnAirplaneModeTitle("No Internet");
        properties.setOnAirplaneModeMessage("You have turned on the airplane mode.");
        properties.setPleaseTurnOffText("Please turn off");
        properties.setAirplaneModeOffButtonText("Airplane mode");
        properties.setShowAirplaneModeOffButtons(true);

        builder.build();
    }

    // Method to toggle password visibility
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password and change drawable to pass_eye_close
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.pass_eye_close, 0);
        } else {
            // Show password and change drawable to pass_eye_open
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.pass_eye_open, 0);
        }
        // Move cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
}
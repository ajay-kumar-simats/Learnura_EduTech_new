package com.example.learnura;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {

    TextView back_to_tea;

    EditText code;

    Button admin_login;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        admin_login = findViewById(R.id.admin_login_btn);

        back_to_tea = findViewById(R.id.back_to_teachers);

        code = findViewById(R.id.secret_code);

        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.getText().toString().equals("SECRET123")) {
                    Intent intent = new Intent(AdminLoginActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                    Toast.makeText(AdminLoginActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AdminLoginActivity.this, "Invalid Admin Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_to_tea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminLoginActivity.this,TeachersLoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
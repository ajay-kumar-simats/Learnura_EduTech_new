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

import com.google.android.gms.common.internal.AccountType;
import com.google.android.material.internal.VisibilityAwareImageButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    TextView name;

    TextView mail;

    EditText et_mail;
    private FirebaseAuth auth;

    private FirebaseDatabase database;

    private DatabaseReference usersRef;

    ImageView back_ep;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        name = findViewById(R.id.ep_name);
        mail = findViewById(R.id.ep_mail);

        et_mail = findViewById(R.id.mail_et);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        back_ep = findViewById(R.id.back_ep_to_settings);

        String userId = auth.getCurrentUser().getUid();
        usersRef.child(userId).child("username").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String username = task.getResult().getValue(String.class);

                name.setText(username);

            } else {
                Toast.makeText(this, "Failed to fetch username!", Toast.LENGTH_SHORT).show();
            }
        });

        usersRef.child(userId).child("email").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()){
                String profile_mail = task.getResult().getValue(String.class);
                mail.setText(profile_mail);
                et_mail.setHint(profile_mail);
            }else{
                Toast.makeText(this, "Failed to fetch email!", Toast.LENGTH_SHORT).show();
            }
        });

        back_ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }
    }
}
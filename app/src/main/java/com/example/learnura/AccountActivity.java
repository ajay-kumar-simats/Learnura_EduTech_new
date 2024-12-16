package com.example.learnura;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    TextView e1txt;

    TextView acc_mail;

    TextView et_profile_name;

    private FirebaseAuth auth;

    private FirebaseDatabase database;

    private DatabaseReference usersRef;

    TextView logout;
    LinearLayout home, achievements, aura, account, l_curve;

    Button Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        et_profile_name = findViewById(R.id.et_profile_name_txt);
        acc_mail = findViewById(R.id.account_mail);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");



        Edit = findViewById(R.id.edit_profile_btn);

        logout = findViewById(R.id.logout_txt);

        // Initialize TextViews for the options
        TextView favoritesText = findViewById(R.id.favorites_text);
        TextView inviteText = findViewById(R.id.invite_text);
        TextView feedbackText = findViewById(R.id.feedback_text);
        TextView updateText = findViewById(R.id.update_text);
        TextView privacyText = findViewById(R.id.privacy_text);
        TextView aboutText = findViewById(R.id.about_text);

        // Initialize ImageView arrows for each option
        ImageView arrow_favourites = findViewById(R.id.arrowFavourites);
        ImageView arrow_invite = findViewById(R.id.arrowInviteFriend);
        ImageView arrow_feedback = findViewById(R.id.arrowFeedback);
        ImageView arrow_update = findViewById(R.id.arrowAppUpdates);
        ImageView arrow_privacy = findViewById(R.id.arrowPrivacyPolicy);
        ImageView arrow_about = findViewById(R.id.arrowAbout);

        String userId = auth.getCurrentUser().getUid();
        usersRef.child(userId).child("username").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        String username = task.getResult().getValue(String.class);
                        et_profile_name.setText(username);
                    } else {
                        Toast.makeText(this, "Failed to fetch username!", Toast.LENGTH_SHORT).show();
                    }
                });

        usersRef.child(userId).child("email").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String a_mail = task.getResult().getValue(String.class);
                acc_mail.setText(a_mail);
            } else {
                Toast.makeText(this, "Failed to fetch email!", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this,EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for each arrow to open details
        arrow_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingDetailActivity("favorites");
            }
        });
        arrow_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingDetailActivity("invite");
            }
        });
        arrow_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingDetailActivity("feedback");
            }
        });
        arrow_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingDetailActivity("update");
            }
        });
        arrow_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingDetailActivity("privacy");
            }
        });
        arrow_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingDetailActivity("about");
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        home = findViewById(R.id.home);
        achievements = findViewById(R.id.achievements);
        aura = findViewById(R.id.aura);
        account = findViewById(R.id.account);
        l_curve = findViewById(R.id.learning_curve);
        e1txt = findViewById(R.id.EmailTextView);

        // Set email click listener
        e1txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = "ajashiatechnologies@gmail.com";
                String subject = "Feedback / Suggestion";
                String message = "Kindly write your valuable message here!";

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + emailAddress));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, message);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AccountActivity.this, "No email client is installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Drawer menu setup
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AccountActivity.this, MainActivity.class);
            }
        });
        achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AccountActivity.this, AchievementsActivity.class);
            }
        });
        aura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(AccountActivity.this, AuraActivity.class);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        l_curve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(AccountActivity.this, LearningCurveActivity.class);
            }
        });

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#813FF1"));
        }
    }

    private void openSettingDetailActivity(String settingType) {
        Intent intent = new Intent(AccountActivity.this, SettingDetailActivity.class);
        intent.putExtra("SETTING_TYPE", settingType);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}

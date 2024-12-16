package com.example.learnura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class SettingDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_detail);

        ImageView back_setting;

        back_setting = findViewById(R.id.back_settings_detail);

        back_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingDetailActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });

        // Get the setting type passed from AccountActivity
        String settingType = getIntent().getStringExtra("SETTING_TYPE");

        // Load the corresponding fragment based on the setting type
        if (settingType != null) {
            Fragment fragment = null;
            switch (settingType) {
                case "favorites":
                    fragment = new FavoritesFragment();
                    break;
                case "invite":
                    fragment = new InviteFriendFragment();
                    break;
                case "feedback":
                    fragment = new FeedbackFragment();
                    break;
                case "update":
                    fragment = new UpdateFragment();
                    break;
                case "privacy":
                    fragment = new PrivacyFragment();
                    break;
                case "about":
                    fragment = new AboutFragment();
                    break;
                // Add other cases for more settings
            }

            // Replace the fragment in FrameLayout
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

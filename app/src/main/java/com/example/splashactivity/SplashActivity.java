package com.example.splashactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = getSharedPreferences("motovista_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        // Splash delay 2 seconds
        new Handler().postDelayed(() -> {
            if (userId == -1) {
                // User not logged in → go to main (login page)
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // User already logged in → go to home
                startActivity(new Intent(SplashActivity.this, UserHomeActivity.class));
            }
            finish();
        }, 2000);
    }
}

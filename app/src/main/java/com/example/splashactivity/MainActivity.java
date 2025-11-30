package com.example.splashactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button userLoginBtn, adminLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If already logged in → Go to home screen directly
        SharedPreferences prefs = getSharedPreferences("motovista_prefs", MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean("logged_in", false);

        if (loggedIn) {
            startActivity(new Intent(MainActivity.this, UserHomeActivity.class));
            finish();
            return;
        }

        // Otherwise show welcome screen
        setContentView(R.layout.activity_main);

        userLoginBtn = findViewById(R.id.userLoginBtn);
        adminLoginBtn = findViewById(R.id.adminLoginBtn);

        userLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, UserLoginActivity.class));
        });

        adminLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
        });
    }
}

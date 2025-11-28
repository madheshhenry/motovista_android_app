package com.example.splashactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button userLoginBtn, adminLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

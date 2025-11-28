package com.example.splashactivity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.splashactivity.R;
import com.example.splashactivity.MainActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout btnCustomerDetails, btnEmiLedger, btnRegisterLedger, btnInsuranceDetails,
            btnNotification, btnApplication, btnNewBikes, btnSecondHandBikes, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnCustomerDetails = findViewById(R.id.btnCustomerDetails);
        btnEmiLedger = findViewById(R.id.btnEmiLedger);
        btnRegisterLedger = findViewById(R.id.btnRegisterLedger);
        btnInsuranceDetails = findViewById(R.id.btnInsuranceDetails);
        btnNotification = findViewById(R.id.btnNotification);
        btnApplication = findViewById(R.id.btnApplication);
        btnNewBikes = findViewById(R.id.btnNewBikes);
        btnSecondHandBikes = findViewById(R.id.btnSecondHandBikes);
        btnLogout = findViewById(R.id.btnLogout);

        btnCustomerDetails.setOnClickListener(v ->
                startActivity(new Intent(this, CustomerDetailsActivity.class)));

        btnEmiLedger.setOnClickListener(v ->
                startActivity(new Intent(this, EmiLedgerActivity.class)));

        btnRegisterLedger.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterLedgerActivity.class)));

        btnInsuranceDetails.setOnClickListener(v ->
                startActivity(new Intent(this, InsuranceDetailsActivity.class)));

        btnNotification.setOnClickListener(v ->
                startActivity(new Intent(this, NotificationActivity.class)));

        btnApplication.setOnClickListener(v ->
                startActivity(new Intent(this, ApplicationActivity.class)));

        btnNewBikes.setOnClickListener(v ->
                startActivity(new Intent(this, NewBikesActivity.class)));

        btnSecondHandBikes.setOnClickListener(v ->
                startActivity(new Intent(this, SecondHandBikesActivity.class)));

        // 🔥 Correct Logout
        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("APP_PREF", MODE_PRIVATE)
                    .edit()
                    .putBoolean("ADMIN_LOGGED_IN", false)
                    .apply();

            startActivity(new Intent(AdminDashboardActivity.this, MainActivity.class));
            finish();
        });
    }
}

package com.example.splashactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.DefaultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {

    EditText etAdminUser, etAdminPass;
    Button btnAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        etAdminUser = findViewById(R.id.etAdminUser);
        etAdminPass = findViewById(R.id.etAdminPass);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);

        btnAdminLogin.setOnClickListener(v -> adminLogin());
    }

    private void adminLogin() {

        String user = etAdminUser.getText().toString().trim();
        String pass = etAdminPass.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Enter username & password", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<DefaultResponse> call = api.adminLogin(user, pass);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    DefaultResponse res = response.body();

                    Toast.makeText(AdminLoginActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();

                    if (res.getStatus().equals("success")) {
                        startActivity(new Intent(AdminLoginActivity.this, MainActivity.class));
                        finish();
                    }

                } else {
                    Toast.makeText(AdminLoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(AdminLoginActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

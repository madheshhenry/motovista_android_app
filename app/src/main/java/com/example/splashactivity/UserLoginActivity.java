package com.example.splashactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.DefaultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvForgotPassword, tvCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        etEmail.startAnimation(fadeIn);
        etPassword.startAnimation(fadeIn);
        btnLogin.startAnimation(fadeIn);

        btnLogin.setOnClickListener(v -> loginUser());
        tvCreateAccount.setOnClickListener(v ->
                startActivity(new Intent(UserLoginActivity.this, CreateAccountActivity.class))
        );
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email & password", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<DefaultResponse> call = api.loginUser(email, password);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                Log.d("LOGIN", "HTTP Code: " + response.code());

                if (!response.isSuccessful()) {
                    Toast.makeText(UserLoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                DefaultResponse resp = response.body();
                if (resp == null) {
                    Toast.makeText(UserLoginActivity.this, "Empty response", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!resp.getStatus().equals("success")) {
                    Toast.makeText(UserLoginActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save user ID
                SharedPreferences prefs = getSharedPreferences("motovista_prefs", MODE_PRIVATE);
                prefs.edit().putInt("user_id", resp.getUserId()).apply();


                Toast.makeText(UserLoginActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();

                startActivity(new Intent(UserLoginActivity.this,UserHomeActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(UserLoginActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

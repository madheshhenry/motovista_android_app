package com.example.splashactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splashactivity.adapters.BikeAdapter;
import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.BikeModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<BikeModel> bikes = new ArrayList<>();
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        recycler = findViewById(R.id.recyclerBikes);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));

        bottomNav = findViewById(R.id.bottomNav);

        // highlight Home icon by default
        bottomNav.setSelectedItemId(R.id.nav_home);

        loadBikes();
        setupBottomNav();
    }

    private void loadBikes() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getBikes().enqueue(new Callback<List<BikeModel>>() {
            @Override
            public void onResponse(Call<List<BikeModel>> call, Response<List<BikeModel>> response) {
                bikes = response.body();
                recycler.setAdapter(new BikeAdapter(UserHomeActivity.this, bikes));
            }

            @Override
            public void onFailure(Call<List<BikeModel>> call, Throwable t) {
                Toast.makeText(UserHomeActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNav() {

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true; // already in home screen
            }
            else if (id == R.id.nav_category) {
                startActivity(new Intent(UserHomeActivity.this, CategoryActivity.class));
            }
            else if (id == R.id.nav_emi) {
                startActivity(new Intent(UserHomeActivity.this, EmiCalculatorActivity.class));
            }
            else if (id == R.id.nav_notify) {
                startActivity(new Intent(UserHomeActivity.this, NotificationActivity.class));
            }
            else if (id == R.id.nav_profile) {
                startActivity(new Intent(UserHomeActivity.this, UserProfileActivity.class));
            }

            overridePendingTransition(0, 0);
            return true;
        });
    }
}

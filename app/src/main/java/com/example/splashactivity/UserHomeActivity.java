package com.example.splashactivity;
import java.util.ArrayList;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<BikeModel> bikes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        recycler = findViewById(R.id.recyclerBikes);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));

        loadBikes();
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

}

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeActivity extends AppCompatActivity {

    RecyclerView recyclerBikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        recyclerBikes = findViewById(R.id.recyclerBikes);
        recyclerBikes.setLayoutManager(new GridLayoutManager(this, 2));

        loadBikes();
    }

    private void loadBikes() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<List<BikeModel>> call = api.getBikes();

        call.enqueue(new Callback<List<BikeModel>>() {
            @Override
            public void onResponse(Call<List<BikeModel>> call, Response<List<BikeModel>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    BikeAdapter adapter = new BikeAdapter(
                            UserHomeActivity.this,
                            response.body(),
                            bike -> {
                                Intent i = new Intent(UserHomeActivity.this, BikeDetailsActivity.class);
                                i.putExtra("bike_id", bike.getId());
                                startActivity(i);
                            });

                    recyclerBikes.setAdapter(adapter);

                } else {
                    Toast.makeText(UserHomeActivity.this,
                            "No bikes found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BikeModel>> call, Throwable t) {
                Toast.makeText(UserHomeActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

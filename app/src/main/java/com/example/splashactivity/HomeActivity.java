package com.example.splashactivity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.splashactivity.adapters.BikesAdapter;
import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.Bike;
import com.example.splashactivity.models.BikeResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rv;
    List<Bike> bikes = new ArrayList<>();
    BikesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rv = findViewById(R.id.rvBikes);
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new BikesAdapter(this, bikes);
        rv.setAdapter(adapter);

        loadBikes();
    }

    private void loadBikes() {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getAllBikes().enqueue(new Callback<BikeResponse>() {
            @Override
            public void onResponse(Call<BikeResponse> call, Response<BikeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bikes.addAll(response.body().getBikes());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<BikeResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
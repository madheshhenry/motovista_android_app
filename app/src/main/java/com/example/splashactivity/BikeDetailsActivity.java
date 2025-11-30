package com.example.splashactivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.splashactivity.adapters.ImageSliderAdapter;
import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.BikeDetailsResponse;
import com.example.splashactivity.models.BikeModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeDetailsActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.0.104/motovista_api/";

    private BikeModel bike;
    private ViewPager2 imageSlider;

    private TextView txtBrand, txtModel, txtPrice, txtEngine, txtDescription;

    private List<String> imageUrls = new ArrayList<>();
    private ImageSliderAdapter sliderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_details);

        imageSlider     = findViewById(R.id.imageSlider);
        txtBrand        = findViewById(R.id.txtBrandModel);
        txtModel        = findViewById(R.id.txtModel);
        txtPrice        = findViewById(R.id.txtPrice);
        txtEngine       = findViewById(R.id.txtEngine);
        txtDescription  = findViewById(R.id.txtDescription);

        String bikeId = getIntent().getStringExtra("bike_id");
        if (bikeId == null || bikeId.trim().isEmpty()) {
            Toast.makeText(this, "Bike ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadBikeDetails(bikeId);
    }

    private void loadBikeDetails(String bikeId) {
        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getBikeDetails(bikeId).enqueue(new Callback<BikeDetailsResponse>() {
            @Override
            public void onResponse(Call<BikeDetailsResponse> call, Response<BikeDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    bike = response.body().getBike();
                    bindBikeToUI(bike);
                } else {
                    Toast.makeText(BikeDetailsActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BikeDetailsResponse> call, Throwable t) {
                Toast.makeText(BikeDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindBikeToUI(BikeModel bike) {

        txtBrand.setText(bike.getBrand());
        txtModel.setText(bike.getModel());
        txtPrice.setText("₹ " + bike.getPrice());
        txtEngine.setText("Engine: " + bike.getEngine());
        txtDescription.setText(bike.getDescription());

        // images
        imageUrls.clear();
        addImageIfExists(bike.getImg_top());
        addImageIfExists(bike.getImg_front());
        addImageIfExists(bike.getImg_left());
        addImageIfExists(bike.getImg_right());

        sliderAdapter = new ImageSliderAdapter(this, imageUrls);
        imageSlider.setAdapter(sliderAdapter);
    }

    private void addImageIfExists(String path) {
        if (path != null && !path.trim().isEmpty()) {
            imageUrls.add(BASE_URL + path);
        }
    }
}

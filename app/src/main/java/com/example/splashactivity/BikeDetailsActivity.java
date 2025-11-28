package com.example.splashactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.BikeModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeDetailsActivity extends AppCompatActivity {

    ImageView imgMainBike;
    TextView tvBrand, tvModel, tvPrice, tvDescription;
    Button btnContact;
    String bikeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_details);

        imgMainBike = findViewById(R.id.imgMainBike);
        tvBrand = findViewById(R.id.tvBrand);
        tvModel = findViewById(R.id.tvModel);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        btnContact = findViewById(R.id.btnContact);

        bikeId = getIntent().getStringExtra("bike_id");

        loadBikeDetails();

        btnContact.setOnClickListener(v -> {
            String phone = "+91XXXXXXXXXX";  // Your showroom contact number
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(intent);
        });
    }

    private void loadBikeDetails() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        Call<BikeModel> call = api.getBikeDetails(bikeId);

        call.enqueue(new Callback<BikeModel>() {
            @Override
            public void onResponse(Call<BikeModel> call, Response<BikeModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BikeModel bike = response.body();

                    Glide.with(BikeDetailsActivity.this)
                            .load(bike.getImage())
                            .into(imgMainBike);

                    tvBrand.setText(bike.getBrand());
                    tvModel.setText(bike.getModel());
                    tvPrice.setText("₹ " + bike.getPrice());
                    tvDescription.setText(bike.getDescription());

                } else {
                    Toast.makeText(BikeDetailsActivity.this,
                            "No details found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BikeModel> call, Throwable t) {
                Toast.makeText(BikeDetailsActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.example.splashactivity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.splashactivity.adapters.ImageSliderAdapter;
import com.example.splashactivity.models.BikeModel;

import java.util.ArrayList;
import java.util.List;

public class BikeDetailsActivity extends AppCompatActivity {

    BikeModel bike;
    ViewPager2 imageSlider;

    TextView txtBrandModel, txtPrice, txtEngine, txtDescription;

    String BASE_URL = "http://10.78.84.188/motovista_api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_details);

        bike = (BikeModel) getIntent().getSerializableExtra("bike"); // ✔ correct key

        imageSlider = findViewById(R.id.imageSlider);
        txtBrandModel = findViewById(R.id.txtBrandModel);
        txtPrice = findViewById(R.id.txtPrice);
        txtEngine = findViewById(R.id.txtEngine);
        txtDescription = findViewById(R.id.txtDescription);

        txtBrandModel.setText(bike.getBrand() + " " + bike.getModel());
        txtPrice.setText("₹ " + bike.getPrice());
        txtEngine.setText(bike.getEngine());
        txtDescription.setText(bike.getDescription());

        List<String> imgs = new ArrayList<>();
        imgs.add(BASE_URL + bike.getImg_top());
        imgs.add(BASE_URL + bike.getImg_front());
        imgs.add(BASE_URL + bike.getImg_left());
        imgs.add(BASE_URL + bike.getImg_right());

        ImageSliderAdapter adapter = new ImageSliderAdapter(this, imgs);
        imageSlider.setAdapter(adapter);
    }
}

package com.example.splashactivity.admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.splashactivity.R;
import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.DefaultResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBikesActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION = 101;

    private Uri uriTop, uriFront, uriLeft, uriRight;

    private ImageView imgTopView, imgFrontView, imgLeftView, imgRightView;
    private EditText etBrand, etModel, etPrice, etEngine, etDescription;
    private Button btnUploadBike;

    private int pickCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bikes);

        imgTopView = findViewById(R.id.imgTopView);
        imgFrontView = findViewById(R.id.imgFrontView);
        imgLeftView = findViewById(R.id.imgLeftView);
        imgRightView = findViewById(R.id.imgRightView);

        etBrand = findViewById(R.id.etBrand);
        etModel = findViewById(R.id.etModel);
        etPrice = findViewById(R.id.etPrice);
        etEngine = findViewById(R.id.etEngine);
        etDescription = findViewById(R.id.etDescription);

        btnUploadBike = findViewById(R.id.btnUploadBike);

        imgTopView.setOnClickListener(v -> pickImage(1));
        imgFrontView.setOnClickListener(v -> pickImage(2));
        imgLeftView.setOnClickListener(v -> pickImage(3));
        imgRightView.setOnClickListener(v -> pickImage(4));

        btnUploadBike.setOnClickListener(v -> uploadBike());
    }

    // ---------- IMAGE PICK ----------
    private void pickImage(int code) {
        if (!checkPermission()) {
            requestPermission();
            return;
        }
        pickCode = code;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri img = result.getData().getData();
                    if (img == null) return;

                    switch (pickCode) {
                        case 1:
                            uriTop = img;
                            imgTopView.setImageURI(img);
                            break;
                        case 2:
                            uriFront = img;
                            imgFrontView.setImageURI(img);
                            break;
                        case 3:
                            uriLeft = img;
                            imgLeftView.setImageURI(img);
                            break;
                        case 4:
                            uriRight = img;
                            imgRightView.setImageURI(img);
                            break;
                    }
                }
            });

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted. Now select image again.", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------- UPLOAD ----------
    private void uploadBike() {

        if (uriTop == null || uriFront == null || uriLeft == null || uriRight == null) {
            Toast.makeText(this, "Select all 4 images", Toast.LENGTH_SHORT).show();
            return;
        }

        String brand = etBrand.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String engine = etEngine.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (brand.isEmpty() || model.isEmpty() || price.isEmpty() ||
                engine.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);

        MultipartBody.Part imgTop = createPartFromUri("img_top", uriTop);
        MultipartBody.Part imgFront = createPartFromUri("img_front", uriFront);
        MultipartBody.Part imgLeft = createPartFromUri("img_left", uriLeft);
        MultipartBody.Part imgRight = createPartFromUri("img_right", uriRight);

        if (imgTop == null || imgFront == null || imgLeft == null || imgRight == null) {
            Toast.makeText(this, "Image read error", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody rbBrand = RequestBody.create(MediaType.parse("text/plain"), brand);
        RequestBody rbModel = RequestBody.create(MediaType.parse("text/plain"), model);
        RequestBody rbPrice = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody rbEngine = RequestBody.create(MediaType.parse("text/plain"), engine);
        RequestBody rbDesc = RequestBody.create(MediaType.parse("text/plain"), desc);

        btnUploadBike.setEnabled(false);

        api.addBike(imgTop, imgFront, imgLeft, imgRight,
                        rbBrand, rbModel, rbPrice, rbEngine, rbDesc)
                .enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        btnUploadBike.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(NewBikesActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                String error = response.errorBody().string();
                                Toast.makeText(NewBikesActivity.this, "SERVER: " + error, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(NewBikesActivity.this, "Unknown server error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }



                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        btnUploadBike.setEnabled(true);
                        Toast.makeText(NewBikesActivity.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ---------- CREATE MULTIPART PART ----------
    private MultipartBody.Part createPartFromUri(String key, Uri uri) {
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            if (is == null) return null;

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = is.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            is.close();

            byte[] data = bos.toByteArray();
            RequestBody req = RequestBody.create(MediaType.parse("image/*"), data);
            return MultipartBody.Part.createFormData(key, key + ".jpg", req);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.example.splashactivity.admin;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.splashactivity.R;
import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.DefaultResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBikesActivity extends AppCompatActivity {

    private static final int REQ_TOP = 1;
    private static final int REQ_FRONT = 2;
    private static final int REQ_LEFT = 3;
    private static final int REQ_RIGHT = 4;

    ImageView imgTopView, imgFrontView, imgLeftView, imgRightView;
    EditText etBrand, etModel, etPrice, etEngine, etDescription;
    Button btnUploadBike;

    Uri uriTop, uriFront, uriLeft, uriRight;

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

        imgTopView.setOnClickListener(v -> pickImage(REQ_TOP));
        imgFrontView.setOnClickListener(v -> pickImage(REQ_FRONT));
        imgLeftView.setOnClickListener(v -> pickImage(REQ_LEFT));
        imgRightView.setOnClickListener(v -> pickImage(REQ_RIGHT));

        btnUploadBike.setOnClickListener(v -> uploadBike());
    }

    private void pickImage(int reqCode) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selected = data.getData();
            if (selected == null) return;

            switch (requestCode) {
                case REQ_TOP:
                    uriTop = selected;
                    imgTopView.setImageURI(uriTop);
                    break;
                case REQ_FRONT:
                    uriFront = selected;
                    imgFrontView.setImageURI(uriFront);
                    break;
                case REQ_LEFT:
                    uriLeft = selected;
                    imgLeftView.setImageURI(uriLeft);
                    break;
                case REQ_RIGHT:
                    uriRight = selected;
                    imgRightView.setImageURI(uriRight);
                    break;
            }
        }
    }

    private void uploadBike() {
        String brand = etBrand.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String engine = etEngine.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (brand.isEmpty() || model.isEmpty() || price.isEmpty() || engine.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (uriTop == null || uriFront == null || uriLeft == null || uriRight == null) {
            Toast.makeText(this, "Select all 4 images", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = ApiClient.getClient().create(ApiService.class);

        MultipartBody.Part topPart = prepareFilePart("img_top", uriTop);
        MultipartBody.Part frontPart = prepareFilePart("img_front", uriFront);
        MultipartBody.Part leftPart = prepareFilePart("img_left", uriLeft);
        MultipartBody.Part rightPart = prepareFilePart("img_right", uriRight);

        RequestBody rbBrand = RequestBody.create(MediaType.parse("text/plain"), brand);
        RequestBody rbModel = RequestBody.create(MediaType.parse("text/plain"), model);
        RequestBody rbPrice = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody rbEngine = RequestBody.create(MediaType.parse("text/plain"), engine);
        RequestBody rbDesc = RequestBody.create(MediaType.parse("text/plain"), desc);

        Call<DefaultResponse> call = api.addBike(
                topPart, frontPart, leftPart, rightPart,
                rbBrand, rbModel, rbPrice, rbEngine, rbDesc
        );

        btnUploadBike.setEnabled(false);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                btnUploadBike.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(NewBikesActivity.this,
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewBikesActivity.this,
                            "Upload failed", Toast.LENGTH_SHORT).show();
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

    private MultipartBody.Part prepareFilePart(String partName, Uri uri) {
        String path = getRealPathFromUri(uri);
        if (path == null) return null;

        File file = new File(path);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), reqFile);
    }

    private String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(idx);
            cursor.close();
            return result;
        }
        return null;
    }
}

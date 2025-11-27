package com.example.splashactivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splashactivity.api.ApiClient;
import com.example.splashactivity.api.ApiService;
import com.example.splashactivity.models.DefaultResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileSetupActivity extends AppCompatActivity {

    private static final int REQ_PROFILE = 101;
    private static final int REQ_AADHAR_FRONT = 102;
    private static final int REQ_AADHAR_BACK = 103;
    private static final int REQ_PAN = 104;

    ImageView ivProfilePhoto, ivAadharFront, ivAadharBack, ivPan;
    Button btnPickProfilePhoto, btnPickAadharFront, btnPickAadharBack, btnPickPan, btnSaveProfile;
    EditText etFullName, etAddress1, etAddress2, etPhone, etEmail, etDob, etCity, etState, etPincode;
    Switch switchEmi, switchDelivery, switchRegistration, switchInsurance, switchNumberPlate;

    Uri uriProfile, uriAadharFront, uriAadharBack, uriPan;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        // Views
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        ivAadharFront = findViewById(R.id.ivAadharFront);
        ivAadharBack = findViewById(R.id.ivAadharBack);
        ivPan = findViewById(R.id.ivPan);

        btnPickProfilePhoto = findViewById(R.id.btnPickProfilePhoto);
        btnPickAadharFront = findViewById(R.id.btnPickAadharFront);
        btnPickAadharBack = findViewById(R.id.btnPickAadharBack);
        btnPickPan = findViewById(R.id.btnPickPan);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        etFullName = findViewById(R.id.etFullName);
        etAddress1 = findViewById(R.id.etAddress1);
        etAddress2 = findViewById(R.id.etAddress2);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etDob = findViewById(R.id.etDob);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etPincode = findViewById(R.id.etPincode);

        switchEmi = findViewById(R.id.switchEmi);
        switchDelivery = findViewById(R.id.switchDelivery);
        switchRegistration = findViewById(R.id.switchRegistration);
        switchInsurance = findViewById(R.id.switchInsurance);
        switchNumberPlate = findViewById(R.id.switchNumberPlate);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Uploading...");

        btnPickProfilePhoto.setOnClickListener(v -> pickImage(REQ_PROFILE));
        btnPickAadharFront.setOnClickListener(v -> pickImage(REQ_AADHAR_FRONT));
        btnPickAadharBack.setOnClickListener(v -> pickImage(REQ_AADHAR_BACK));
        btnPickPan.setOnClickListener(v -> pickImage(REQ_PAN));

        etDob.setOnClickListener(v -> showDatePicker());

        btnSaveProfile.setOnClickListener(v -> {
            try {
                saveProfile();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void pickImage(int requestCode) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select image"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selected = data.getData();
            if (requestCode == REQ_PROFILE) {
                uriProfile = selected;
                ivProfilePhoto.setImageURI(uriProfile);
            } else if (requestCode == REQ_AADHAR_FRONT) {
                uriAadharFront = selected;
                ivAadharFront.setImageURI(uriAadharFront);
            } else if (requestCode == REQ_AADHAR_BACK) {
                uriAadharBack = selected;
                ivAadharBack.setImageURI(uriAadharBack);
            } else if (requestCode == REQ_PAN) {
                uriPan = selected;
                ivPan.setImageURI(uriPan);
            }
        }
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR), mm = c.get(Calendar.MONTH), dd = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, (DatePicker view, int year, int month, int dayOfMonth) -> {
            String dobStr = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            etDob.setText(dobStr);
        }, yy, mm, dd);
        dpd.show();
    }

    private void saveProfile() {
        // basic validation
        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String address1 = etAddress1.getText().toString().trim();
        String address2 = etAddress2.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String pincode = etPincode.getText().toString().trim();

        if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty() || address1.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // get user_id from SharedPreferences (set earlier after login)
        SharedPreferences prefs = getSharedPreferences("motovista_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not recognized. Login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.show();

        // prepare RequestBody parts
        ApiService api = ApiClient.getClient().create(ApiService.class);

        RequestBody rbUserId = RequestBody.create(String.valueOf(userId), MediaType.parse("text/plain"));
        RequestBody rbFullName = RequestBody.create(fullName, MediaType.parse("text/plain"));
        RequestBody rbPhone = RequestBody.create(phone, MediaType.parse("text/plain"));
        RequestBody rbEmail = RequestBody.create(email, MediaType.parse("text/plain"));
        RequestBody rbDob = RequestBody.create(dob, MediaType.parse("text/plain"));
        RequestBody rbAddress1 = RequestBody.create(address1, MediaType.parse("text/plain"));
        RequestBody rbAddress2 = RequestBody.create(address2, MediaType.parse("text/plain"));
        RequestBody rbCity = RequestBody.create(city, MediaType.parse("text/plain"));
        RequestBody rbState = RequestBody.create(state, MediaType.parse("text/plain"));
        RequestBody rbPincode = RequestBody.create(pincode, MediaType.parse("text/plain"));

        RequestBody rbEmi = RequestBody.create(switchEmi.isChecked() ? "1" : "0", MediaType.parse("text/plain"));
        RequestBody rbDelivery = RequestBody.create(switchDelivery.isChecked() ? "1" : "0", MediaType.parse("text/plain"));
        RequestBody rbReg = RequestBody.create(switchRegistration.isChecked() ? "1" : "0", MediaType.parse("text/plain"));
        RequestBody rbInsurance = RequestBody.create(switchInsurance.isChecked() ? "1" : "0", MediaType.parse("text/plain"));
        RequestBody rbNumberPlate = RequestBody.create(switchNumberPlate.isChecked() ? "1" : "0", MediaType.parse("text/plain"));

        MultipartBody.Part partProfile = prepareFilePart("profile_photo", uriProfile);
        MultipartBody.Part partAadharFront = prepareFilePart("aadhar_front", uriAadharFront);
        MultipartBody.Part partAadharBack = prepareFilePart("aadhar_back", uriAadharBack);
        MultipartBody.Part partPan = prepareFilePart("pan_card", uriPan);

        Call<DefaultResponse> call = api.saveProfile(
                rbUserId, rbFullName, rbPhone, rbEmail, rbDob, rbAddress1, rbAddress2, rbCity, rbState, rbPincode,
                partProfile, partAadharFront, partAadharBack, partPan,
                rbEmi, rbDelivery, rbReg, rbInsurance, rbNumberPlate
        );

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progress.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    DefaultResponse res = response.body();
                    Toast.makeText(ProfileSetupActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                    if ("success".equalsIgnoreCase(res.getStatus())) {
                        // go to main/home
                        startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(ProfileSetupActivity.this, "Server error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(ProfileSetupActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("ProfileUpload", "onFailure", t);
            }
        });
    }

    // helper to create MultipartBody.Part from Uri
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        if (fileUri == null) return null;

        try {
            File file = createTempFileFromUri(fileUri);
            RequestBody requestFile = RequestBody.create(file, MediaType.parse(getContentResolver().getType(fileUri) == null ? "image/*" : getContentResolver().getType(fileUri)));
            return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // create a temp file in app cache from Uri (works for content and file URIs)
    private File createTempFileFromUri(Uri uri) throws Exception {
        String fileName = queryName(uri);
        File tempFile = new File(getCacheDir(), fileName);
        try (InputStream input = getContentResolver().openInputStream(uri);
             FileOutputStream output = new FileOutputStream(tempFile)) {
            if (input == null) throw new Exception("Cannot open input stream");
            byte[] buffer = new byte[4096];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
        }
        return tempFile;
    }

    // query display name for content URI, fallback to timestamp name
    private String queryName(Uri uri) {
        String result = null;
        try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (idx >= 0) result = cursor.getString(idx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) result = "file_" + System.currentTimeMillis() + ".jpg";
        return result;
    }
}

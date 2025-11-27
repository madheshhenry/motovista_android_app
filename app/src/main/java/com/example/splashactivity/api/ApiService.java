package com.example.splashactivity.api;

import com.example.splashactivity.models.DefaultResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    // -------------------- USER REGISTER --------------------
    @FormUrlEncoded
    @POST("register.php")
    Call<DefaultResponse> registerUser(
            @Field("full_name") String fullName,
            @Field("email") String email,
            @Field("password") String password
    );

    // -------------------- USER LOGIN --------------------
    @FormUrlEncoded
    @POST("login.php")
    Call<DefaultResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    // -------------------- ADMIN LOGIN --------------------
    @FormUrlEncoded
    @POST("admin_login.php")
    Call<DefaultResponse> adminLogin(
            @Field("username") String username,
            @Field("password") String password
    );

    // -------------------- PROFILE SETUP --------------------
    @Multipart
    @POST("save_profile.php")
    Call<DefaultResponse> saveProfile(

            @Part("user_id") RequestBody user_id,
            @Part("full_name") RequestBody fullName,
            @Part("phone") RequestBody phone,
            @Part("email") RequestBody email,
            @Part("dob") RequestBody dob,
            @Part("address1") RequestBody addr1,
            @Part("address2") RequestBody addr2,
            @Part("city") RequestBody city,
            @Part("state") RequestBody state,
            @Part("pincode") RequestBody pincode,

            @Part MultipartBody.Part profile_photo,
            @Part MultipartBody.Part aadhar_front,
            @Part MultipartBody.Part aadhar_back,
            @Part MultipartBody.Part pan_card,

            @Part("emi_notify") RequestBody emi,
            @Part("delivery_notify") RequestBody delivery,
            @Part("reg_notify") RequestBody reg,
            @Part("insurance_notify") RequestBody insurance,
            @Part("numberplate_notify") RequestBody numberplate
    );
    @GET("get_bikes.php")
    Call<BikeResponse> getAllBikes();
}

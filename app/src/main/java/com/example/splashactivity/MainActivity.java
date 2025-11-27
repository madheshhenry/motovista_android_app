package com.example.splashactivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView bikeImg;
    Button userLoginBtn, adminLoginBtn;
    TextView titleRibbon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bikeImg = findViewById(R.id.bikeImg);
        userLoginBtn = findViewById(R.id.userLoginBtn);
        adminLoginBtn = findViewById(R.id.adminLoginBtn);
        titleRibbon = findViewById(R.id.titleRibbon);

        // 1) Load simple XML animations for title and buttons
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation scaleSmall = AnimationUtils.loadAnimation(this, R.anim.scale_small);

        // animate title
        titleRibbon.startAnimation(fadeIn);

        // animate buttons with slight delay
        userLoginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
        adminLoginBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));

        // subtle scale-in for image
        bikeImg.startAnimation(scaleSmall);

        // 2) Pulse animation (infinite subtle breathing)
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bikeImg, "scaleX", 1f, 1.03f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(bikeImg, "scaleY", 1f, 1.03f);
        scaleX.setDuration(1500);
        scaleY.setDuration(1500);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        AnimatorSet pulse = new AnimatorSet();
        pulse.playTogether(scaleX, scaleY);
        pulse.start();

        // 3) Button clicks (navigate to activities)
        userLoginBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserLoginActivity.class)));
        adminLoginBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AdminLoginActivity.class)));
    }
}

package com.beecoder.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private ImageView splashImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashImg=findViewById(R.id.splashImg);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        splashImg.setAnimation(animation1);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               startActivity(new Intent(SplashScreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
               finish();
           }
       },SPLASH_TIME_OUT);/*{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, SPLASH_TIME_OUT);*/
    }
}
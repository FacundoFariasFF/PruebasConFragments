package com.example.fragmentosapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import kotlinx.coroutines.Delay;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_SCREEN_DALAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TimerTask task = new TimerTask() {
            @Override
            public void run(){
                Intent intent = new Intent().setClass(
                        SplashActivity.this,MainActivity.class);
                startActivity(intent);

                //codigo si quisiera controlar el tiempo de carga de la db

                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DALAY);

    }
}
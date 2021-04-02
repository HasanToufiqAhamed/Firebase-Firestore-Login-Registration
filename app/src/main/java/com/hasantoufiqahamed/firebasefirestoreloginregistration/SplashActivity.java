package com.hasantoufiqahamed.firebasefirestoreloginregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences=getSharedPreferences("fake_auth", MODE_PRIVATE);

        String email=sharedPreferences.getString("email", "");

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (email.isEmpty() || email.equals("")){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                finish();
            }
        };
        new Timer().schedule(task, 700);
    }
}
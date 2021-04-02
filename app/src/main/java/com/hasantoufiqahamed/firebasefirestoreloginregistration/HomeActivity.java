package com.hasantoufiqahamed.firebasefirestoreloginregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Home");
        sharedPreferences=getSharedPreferences("fake_auth", MODE_PRIVATE);
        String email=sharedPreferences.getString("email", "");
        TextView emailText = findViewById(R.id.emailText);
        emailText.setText(email);

        findViewById(R.id.logOut).setOnClickListener(logOut->{
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("email", "");
            editor.apply();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }
}
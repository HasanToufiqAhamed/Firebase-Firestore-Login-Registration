package com.hasantoufiqahamed.firebasefirestoreloginregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasantoufiqahamed.firebasefirestoreloginregistration.object.UserInfo;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    public static Activity activity;
    private SharedPreferences sharedPreferences;
    private TextInputLayout signInEmail;
    private TextInputLayout signInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;

        getSupportActionBar().setTitle("Sign in");
        sharedPreferences=getSharedPreferences("fake_auth", MODE_PRIVATE);

        signInEmail=findViewById(R.id.signInEmail);
        signInPassword=findViewById(R.id.signInPassword);

        findViewById(R.id.signInButton).setOnClickListener(signInButton->{
            String email=signInEmail.getEditText().getText().toString().trim();
            String password=signInPassword.getEditText().getText().toString().trim();

            if (email.isEmpty() || !SignUpActivity.isValidEmailAddress(email) || password.isEmpty() ||  password.length()<6){
                if (email.isEmpty()){
                    signInEmail.setError("Enter email");
                    signInEmail.requestFocus();
                } else if (!SignUpActivity.isValidEmailAddress(email)){
                    signInEmail.setError("Enter valid email");
                    signInEmail.requestFocus();
                } else if (password.isEmpty()){
                    signInPassword.setError("Enter password");
                    signInPassword.requestFocus();
                } else {
                    signInPassword.setError("Enter long password");
                    signInPassword.requestFocus();
                }
            } else {
                signIn(email, password);
            }
        });

        findViewById(R.id.noAccount).setOnClickListener(noAccount->{
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        });
    }

    private void signIn(String email, String password) {
        database.collection("account").document(email).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()){
                Toast.makeText(getApplicationContext(), "This user not exist", Toast.LENGTH_SHORT).show();
            } else {
                UserInfo userInfo=documentSnapshot.toObject(UserInfo.class);

                if (userInfo.password.equals(password)){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
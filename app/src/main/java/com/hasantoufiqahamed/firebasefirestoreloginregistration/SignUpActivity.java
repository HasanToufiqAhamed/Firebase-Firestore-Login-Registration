package com.hasantoufiqahamed.firebasefirestoreloginregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hasantoufiqahamed.firebasefirestoreloginregistration.object.UserInfo;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private TextInputLayout signUpEmail;
    private TextInputLayout signUpPassword;
    private TextInputLayout signUpPasswordCheck;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Sign up");
        sharedPreferences=getSharedPreferences("fake_auth", MODE_PRIVATE);

        signUpEmail=findViewById(R.id.signUpEmail);
        signUpPassword=findViewById(R.id.signUpPassword);
        signUpPasswordCheck=findViewById(R.id.signUpPasswordCheck);

        findViewById(R.id.button).setOnClickListener(button->{
            String email=signUpEmail.getEditText().getText().toString().trim();
            String password=signUpPassword.getEditText().getText().toString().trim();
            String password2=signUpPasswordCheck.getEditText().getText().toString().trim();

            if (email.isEmpty() || !isValidEmailAddress(email) || password.isEmpty() || password2.isEmpty() || password.length()<6 || !password.equals(password2)){
                if (email.isEmpty()){
                    signUpEmail.setError("Enter email");
                    signUpEmail.requestFocus();
                } else if (!isValidEmailAddress(email)) {
                    signUpEmail.setError("Enter valid email");
                    signUpEmail.requestFocus();
                } else if (password.isEmpty()){
                    signUpPassword.setError("Enter password");
                    signUpPassword.requestFocus();
                } else if (password2.isEmpty()){
                    signUpPasswordCheck.setError("Enter password");
                    signUpPasswordCheck.requestFocus();
                } else if (password.length()<6){
                    signUpPassword.setError("Enter long password");
                    signUpPassword.requestFocus();
                } else {
                    signUpPasswordCheck.setError("Password not match");
                    signUpPasswordCheck.requestFocus();
                }
            } else {
                createAccount(email, password);
            }
        });
    }

    private void createAccount(String email, String password) {
        database.collection("account").document(email).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()){
                database.collection("account").document(email).set(new UserInfo(email, password)).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.apply();

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        MainActivity.activity.finish();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "User already registered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
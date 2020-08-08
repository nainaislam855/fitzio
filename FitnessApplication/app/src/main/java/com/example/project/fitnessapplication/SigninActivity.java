package com.example.project.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.fitnessapplication.db.DatabaseHelper;
import com.example.project.fitnessapplication.entities.UserObject;
import com.google.android.material.snackbar.Snackbar;

public class SigninActivity extends AppCompatActivity {
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }

    public void authenticate(View v) {
        String pass;

        EditText emaill = findViewById(R.id.email);
        email = emaill.getText().toString();
        EditText password = findViewById(R.id.password);
        pass = password.getText().toString();

        UserObject user = new UserObject();
        user.setEmail(email);
        user.setPassword(pass);


        boolean isLoginSuccessful = DatabaseHelper.getInstance(this).login(user);

        if (isLoginSuccessful) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Login Unsuccessful, Please Try Again", Snackbar.LENGTH_LONG).show();
        }
    }

    public void signup(View v) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();

    }

}







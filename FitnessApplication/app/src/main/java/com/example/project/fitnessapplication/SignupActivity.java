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

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void register(View v) {

        String em, password, conf;
        EditText email = findViewById(R.id.email);
        em = email.getText().toString();
        EditText pass = findViewById(R.id.password);
        password = pass.getText().toString();
        EditText confirm = findViewById(R.id.confirm);
        conf = confirm.getText().toString();

        if (!(em.equals("") || password.equals("") || conf.equals(""))) {
            if (isValidEmailAddress(em)) {
                if (password.equals(conf)) {

                    UserObject user=new UserObject();
                    user.setEmail(em);
                    user.setPassword(password);
                    boolean doesUserExist= DatabaseHelper.getInstance(this).signUp(user);

                    if (doesUserExist){
                        Snackbar.make(findViewById(android.R.id.content), "This email already exists, please sign up with a new one", Snackbar.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(this, "Signup Successful", Toast.LENGTH_LONG).show();
                        finish();
                        Intent i = new Intent(this, SigninActivity.class);
                        startActivity(i);
                    }

                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Password and Confirm Password Doesn't Match", Snackbar.LENGTH_LONG).show();
                }

            } else {
                Snackbar.make(findViewById(android.R.id.content), "Please Enter a Valid Email", Snackbar.LENGTH_LONG).show();
            }

        } else {
            Snackbar.make(findViewById(android.R.id.content), "All Fields are Required ", Snackbar.LENGTH_LONG).show();
        }
    }

    public void signin(View v) {
        Intent i=new Intent(this, SigninActivity.class);
        startActivity(i);
        finish();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}

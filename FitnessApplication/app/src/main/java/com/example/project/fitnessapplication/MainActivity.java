package com.example.project.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void signin (View v){
        Intent i=new Intent(this, SigninActivity.class);
        startActivity(i);
        finish();

    }

    public void signup (View v){
        Intent i=new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }

}

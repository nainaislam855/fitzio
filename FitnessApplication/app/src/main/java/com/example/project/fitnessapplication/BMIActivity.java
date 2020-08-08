package com.example.project.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.fitnessapplication.db.DatabaseHelper;

public class BMIActivity extends AppCompatActivity {
    EditText height, weight;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        result = findViewById(R.id.result);
    }

    public void calculate(View v) {
        if (!(height.getText().toString().equals("") || weight.getText().toString().equals(""))) {
            double heigh = Double.parseDouble(height.getText().toString());
            double weigh = Double.parseDouble(weight.getText().toString());
            String status=CalculateBMI(heigh, weigh);
            DatabaseHelper.getInstance(this).setDailyBMIStatus(status);
            result.setText(status);
        }
    }

    private String CalculateBMI(double height,double weight) {
        double bmiValue = (weight / Math.pow(height, 2));
        if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue >= 18.5 && bmiValue < 25) {
            return "Normal";
        } else if (bmiValue >= 25 && bmiValue < 30) {
            return "Overweight";
        }
        return "Obese";
    }
    public void finis(View v) {
        finish();
    }

}

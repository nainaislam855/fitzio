package com.example.project.fitnessapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.project.fitnessapplication.db.DatabaseHelper;
import com.example.project.fitnessapplication.entities.BMIStatusHistory;
import com.example.project.fitnessapplication.entities.CalorieIntakeHistory;
import com.example.project.fitnessapplication.entities.DailyHealthReport;
import com.example.project.fitnessapplication.entities.WaterCountHistory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HealthHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_history);
        ListView listView = findViewById(R.id.list);
        List<DailyHealthReport> healthReportList=getHealthReportList();
        HealthHistoryListAdapter adapter = new HealthHistoryListAdapter(this, healthReportList);
        listView.setAdapter(adapter);

    }

    public void finis(View v) {
        finish();
    }


    public List<DailyHealthReport> getHealthReportList() {
        List<DailyHealthReport> dailyHealthReportList = new ArrayList<>();

        List<WaterCountHistory> waterCountHistories = DatabaseHelper.getInstance(this).getDailyWaterCountHistory();
        List<BMIStatusHistory> bmiStatusHistories = DatabaseHelper.getInstance(this).getDailyBMIStatusHistory();
        List<CalorieIntakeHistory> calorieIntakeHistories = DatabaseHelper.getInstance(this).getDailyCalorieCountHistory();

        Set<String> uniqueDates = new HashSet<>();

        for (WaterCountHistory history : waterCountHistories) {
            uniqueDates.add(history.getDate());
        }

        for (BMIStatusHistory history : bmiStatusHistories) {
            uniqueDates.add(history.getDate());
        }

        for (CalorieIntakeHistory history : calorieIntakeHistories) {
            uniqueDates.add(history.getDate());
        }

        for (String date : uniqueDates) {
            DailyHealthReport dailyHealthReport = new DailyHealthReport();
            dailyHealthReport.setDate(date);

            for (WaterCountHistory history : waterCountHistories) {
                if (date.equals(history.getDate())){
                    dailyHealthReport.setWaterCount(history.getCount());
                    break;
                }
            }

            for (BMIStatusHistory history : bmiStatusHistories) {
                if (date.equals(history.getDate())){
                    dailyHealthReport.setBmiStatus(history.getStatus());
                     break;
                }
            }

            for (CalorieIntakeHistory history : calorieIntakeHistories) {
                if (date.equals(history.getDate())){
                    dailyHealthReport.setCalorieCount(history.getCount());
                    break;
                }
            }
            dailyHealthReportList.add(dailyHealthReport);
        }

        return dailyHealthReportList;
    }
}

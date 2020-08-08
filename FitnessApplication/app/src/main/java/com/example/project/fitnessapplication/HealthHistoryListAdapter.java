package com.example.project.fitnessapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.project.fitnessapplication.entities.DailyHealthReport;
import java.util.List;

public class HealthHistoryListAdapter extends ArrayAdapter<DailyHealthReport> {
    private final Activity context;
    private final List<DailyHealthReport> dailyHealthReportList;

    //contructor where you're setting the Daily Health Report List into the adapter
    public HealthHistoryListAdapter(Activity context, List<DailyHealthReport> dailyHealthReportList) {
        super(context, R.layout.health_history_list_row, dailyHealthReportList);
        this.context = context;
        this.dailyHealthReportList = dailyHealthReportList;
    }

    // This method will be called for each list row and the data will be entered in the appropriate location
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.health_history_list_row, null, true);
        TextView dateTextView = rowView.findViewById(R.id.dateTextView);
        TextView waterCountTextView = rowView.findViewById(R.id.waterCountTextView);
        TextView bmiStatusTextView = rowView.findViewById(R.id.bmiStatusTextView);
        TextView calorieCountTextView = rowView.findViewById(R.id.calorieCountTextView);
        DailyHealthReport dailyHealthReport = dailyHealthReportList.get(position);
        dateTextView.setText(dailyHealthReport.getDate());
        waterCountTextView.setText(dailyHealthReport.getWaterCount() + "");
        bmiStatusTextView.setText(dailyHealthReport.getBmiStatus());
        calorieCountTextView.setText(dailyHealthReport.getCalorieCount() + "");

        return rowView;

    }

}

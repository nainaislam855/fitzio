package com.example.project.fitnessapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.fitnessapplication.db.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

public class HomeActivity extends AppCompatActivity {
    TextView waterCount;
    EditText calorieAddField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //for consistent behavior

        BannerSlider bannerSlider = findViewById(R.id.banner_slider1);
        bannerSlider.addBanner(new DrawableBanner(R.drawable.one));
        bannerSlider.addBanner(new DrawableBanner(R.drawable.two));
        bannerSlider.addBanner(new DrawableBanner(R.drawable.three));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //for opening and closing the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //for setting the action on nav_view
        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        View headerView = navigationView.getHeaderView(0);
        //for displaying the icon's default colors
        navigationView.setItemIconTintList(null);
        //setting email of the logged_in user on nav_header
        TextView email=headerView.findViewById(R.id.email);
        SharedPreferences sharedPref = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        email.setText(sharedPref.getString("email", ""));
        //setting previous waterCount of logged_in user
        waterCount =findViewById(R.id.count);
        int waterCountText= DatabaseHelper.getInstance(this).getDailyWaterCount();
        waterCount.setText(waterCountText+"");

        //setting previous calorieCount of logged_in user
        calorieAddField =findViewById(R.id.calorieAddField);
        int calorieCountText= DatabaseHelper.getInstance(this).getDailyCalorieCount();
        calorieAddField.setText(calorieCountText+"");
    }
    //specifying actions for items on drawer
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.home) {

                        }
                        else if (id== R.id.healthHistory){
                            Intent i=new Intent(HomeActivity.this, HealthHistoryActivity.class);
                            startActivity(i);
                        }
                        else if (id== R.id.healthTips){
                            //implicit intent
                            Uri webpage = Uri.parse("https://www.healthline.com/nutrition/27-health-and-nutrition-tips");
                            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            startActivity(webIntent);
                        }
                        else if (id == R.id.logout) {
                            //remove from sharedPreferences
                            SharedPreferences s = getSharedPreferences("LogIn", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = s.edit();
                            editor.remove("id");
                            editor.remove("email");
                            editor.clear();
                            editor.commit();
                            Intent i = new Intent(HomeActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        DrawerLayout drawer = findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void addWater (View v){
        int count= Integer.parseInt(waterCount.getText().toString())+1;
        DatabaseHelper.getInstance(this).setDailyWaterCount(count);
        waterCount.setText(count+"");

    }
    public void subWater (View v){
        int count= Integer.parseInt(waterCount.getText().toString())-1;
        if (count>=0) {
            DatabaseHelper.getInstance(this).setDailyWaterCount(count);
            waterCount.setText(count + "");
        }
        else {
            Toast.makeText(this, "Water Count can't be less than 0", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearWater (View v){
        DatabaseHelper.getInstance(this).setDailyWaterCount(0);
        waterCount.setText(0+"");
    }

    public void bMI (View v){
        Intent i=new Intent(this,BMIActivity.class);
        startActivity(i);
    }

    public void addCalories (View v){
        String calorieText=calorieAddField.getText().toString();
        if (!calorieText.isEmpty()) {
            int count = Integer.parseInt(calorieText);
            DatabaseHelper.getInstance(this).setDailyCalorieCount(count);
            Toast.makeText(this, "Calories Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}

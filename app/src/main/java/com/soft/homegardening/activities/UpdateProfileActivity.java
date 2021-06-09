package com.soft.homegardening.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.soft.homegardening.R;


public class UpdateProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getSupportActionBar().hide();

    }
}
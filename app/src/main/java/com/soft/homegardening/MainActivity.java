package com.soft.homegardening;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.soft.homegardening.activities.DashBoardActivity;

public class MainActivity extends AppCompatActivity {
    /*           FYP PROJECT
     *     PROJECT NAME: HOME GARDENING
     *             GROUP MEMBERS
     *    MATEEN NAWAZ                BCSM-F17-028
     *    SYED MUHAMMAD IBRAHIM WASTI BCSM-F17-038
     *    FAHAD RIAZ                  BCSM-F17-043
     *    HASSAN MAHMOOD              BCSM-F17-031*/
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //how much time you want to display splash screen
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
                finish();
            }
        }, 3000); //for 3 second it will display

    }
}
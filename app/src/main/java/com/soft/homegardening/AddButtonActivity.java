package com.soft.homegardening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddButtonActivity extends AppCompatActivity {
    //declare variable
    CardView addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_button);
        //initialize
        addBtn=findViewById(R.id.addbtn);
        //move to add plant in my garden
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddButtonActivity.this,AddPlantToMyGardenActivity.class));
            }
        });
    }
}
package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavedDataActivity extends AppCompatActivity {

    //declare variable
    String name,key;
    DatabaseReference databaseReference;
    TextView plantName,detail;
    FirebaseAuth firebaseAuth;
    ImageView plantimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_data);
        //initialize variable
        plantName=findViewById(R.id.plant_name_TV);
        detail=findViewById(R.id.detailTv);
        plantimg=findViewById(R.id.detailIV);
        firebaseAuth=FirebaseAuth.getInstance();
        name=getIntent().getStringExtra("name");
        key=getIntent().getStringExtra("key");
        databaseReference= FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid())
        .child("Save Data").child(key);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    //load plant image
                    Glide.with(SavedDataActivity.this).load(snapshot.child("url").getValue().toString()).into(plantimg);
                    //set plant name
                    plantName.setText(snapshot.child("name").getValue(String.class));
                    //set detail of plant
                    detail.setText(snapshot.child("description").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
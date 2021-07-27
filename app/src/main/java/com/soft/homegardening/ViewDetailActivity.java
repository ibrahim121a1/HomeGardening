package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewDetailActivity extends AppCompatActivity {

    //declare variable
    ImageView plantImg,backImg;
    TextView plantName,preparingTv,plantingTv,careTv,fertilizerTv,diseaseTv;
    String key;
    DatabaseReference databaseReference, dbref,dbrefgarden,getDbref;
    String plant, name;
    String geturl, getPlant,getPreparing,getPlanting,getCare,getFertilizer,getDisease,getImage;
    FirebaseAuth firebaseAuth;
    ToggleButton favbtn;
    Button addmygarden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);
        //initialize variable
        plantImg = findViewById(R.id.detailIV);
        plantName = findViewById(R.id.plant_name_TV);
        addmygarden=findViewById(R.id.add_to_my_garden);
        preparingTv=findViewById(R.id.preparingdetailTv);
        plantingTv=findViewById(R.id.PlantingdetailTv);
        careTv=findViewById(R.id.caredetailTv);
        backImg=findViewById(R.id.backimg);
        fertilizerTv=findViewById(R.id.fertilizerdetailTv);
        diseaseTv=findViewById(R.id.pestdetailTv);
        key = getIntent().getStringExtra("key");
        plant = getIntent().getStringExtra("plant");
        name = getIntent().getStringExtra("name");
        databaseReference = FirebaseDatabase.getInstance().getReference(plant);


        firebaseAuth = FirebaseAuth.getInstance();
        dbrefgarden=FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("My Garden");
        favbtn = findViewById(R.id.fav_btn);
        dbref=FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("Favourite").child(name);
        getDbref=FirebaseDatabase.getInstance().getReference("plantimg").child(plant);
        getDbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    getImage=snapshot.child("image").getValue(String.class);
                    Glide.with(getApplication()).load(getImage).into(backImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //show data if exsisit
                    geturl = snapshot.child("url").getValue().toString();
                    getPlant = snapshot.child("name").getValue().toString();
                    getPreparing=snapshot.child("preparing").getValue().toString();
                    getPlanting=snapshot.child("planting").getValue().toString();
                    getCare=snapshot.child("care").getValue().toString();
                    getFertilizer=snapshot.child("fertilizer").getValue().toString();
                    getDisease=snapshot.child("disease").getValue().toString();
                    Glide.with(ViewDetailActivity.this).load(geturl).into(plantImg);
                    plantName.setText(getPlant);
                    preparingTv.setText(getPreparing);
                    plantingTv.setText(getPlanting);
                    careTv.setText(getCare);
                    fertilizerTv.setText(getFertilizer);
                    diseaseTv.setText(getDisease);
                } else {
                    Toast.makeText(ViewDetailActivity.this, "Your Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // add data in my garden
        addmygarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<Object,String>  gardenmap=new HashMap<>();
                gardenmap.put("name",getPlant);
                gardenmap.put("url",geturl);
                gardenmap.put("fertilizer",getFertilizer);
                gardenmap.put("care",getCare);
                gardenmap.put("planting",getPlanting);
                gardenmap.put("preparing",getPreparing);
                gardenmap.put("disease",getDisease);
//                gardenmap.put("seen"," ");
                dbrefgarden.child(getPlant.toLowerCase()).setValue(gardenmap);
                Toast.makeText(ViewDetailActivity.this, "Plant Added in My Garden", Toast.LENGTH_SHORT).show();
            }
        });
        favbtn.setChecked(false);
        favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_notfav));
        favbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // add data to favourite plant
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_fav));
                    Map<Object,String> map=new HashMap<>();
                    map.put("name",getPlant);
                    map.put("url",geturl);
                    map.put("fertilizer",getFertilizer);
                    map.put("care",getCare);
                    map.put("planting",getPlanting);
                    map.put("preparing",getPreparing);
                    map.put("disease",getDisease);
                    dbref.setValue(map);
                }
                else {
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_notfav));
                    dbref.removeValue();
                }

            }
        });
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_fav));
                }
                else
                {
                    favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_notfav));
                    if (snapshot.exists())
                    {
                        favbtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_fav));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
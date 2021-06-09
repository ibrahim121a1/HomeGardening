package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewFavPlantActivity extends AppCompatActivity {

    ImageView favimg,addNotes,viewNotes;
    TextView favtext,preparingTv,plantingTv,careTv,fertilizerTv,diseaseTv;
    String key,name,seen,getPreparing,getPlanting,getCare,getFertilizer,getDisease,getName;
    DatabaseReference databaseReference,dbref;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fav_plant);
        favimg=findViewById(R.id.detailIV);
        favtext=findViewById(R.id.plant_name_TV);
        preparingTv=findViewById(R.id.preparingdetailTv);
        plantingTv=findViewById(R.id.PlantingdetailTv);
        careTv=findViewById(R.id.caredetailTv);
        fertilizerTv=findViewById(R.id.fertilizerdetailTv);
        diseaseTv=findViewById(R.id.pestdetailTv);
        addNotes=findViewById(R.id.addnotesid);
        viewNotes=findViewById(R.id.viewnotes);
        firebaseAuth=FirebaseAuth.getInstance();
        key=getIntent().getStringExtra("key");
        name=getIntent().getStringExtra("name");
        seen=getIntent().getStringExtra("seen");
        databaseReference= FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("Favourite");
        dbref=FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("My Garden");
        if (name.equals("Favourite"))
        {
            databaseReference.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        Glide.with(ViewFavPlantActivity.this).load(snapshot.child("url").getValue().toString()).into(favimg);
                        favtext.setText(snapshot.child("name").getValue().toString());
                        getPreparing=snapshot.child("preparing").getValue().toString();
                        getPlanting=snapshot.child("planting").getValue().toString();
                        getCare=snapshot.child("care").getValue().toString();
                        getFertilizer=snapshot.child("fertilizer").getValue().toString();
                        getDisease=snapshot.child("disease").getValue().toString();
                        preparingTv.setText(getPreparing);
                        plantingTv.setText(getPlanting);
                        careTv.setText(getCare);
                        fertilizerTv.setText(getFertilizer);
                        diseaseTv.setText(getDisease);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (name.equals("My Garden"))
        {
            addNotes.setVisibility(View.VISIBLE);
            viewNotes.setVisibility(View.VISIBLE);

            dbref.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        Glide.with(ViewFavPlantActivity.this).load(snapshot.child("url").getValue().toString()).into(favimg);
                        getName=snapshot.child("name").getValue().toString();
                        favtext.setText(getName);
                        getPreparing=snapshot.child("preparing").getValue().toString();
                        getPlanting=snapshot.child("planting").getValue().toString();
                        getCare=snapshot.child("care").getValue().toString();
                        getFertilizer=snapshot.child("fertilizer").getValue().toString();
                        getDisease=snapshot.child("disease").getValue().toString();
                        preparingTv.setText(getPreparing);
                        plantingTv.setText(getPlanting);
                        careTv.setText(getCare);
                        fertilizerTv.setText(getFertilizer);
                        diseaseTv.setText(getDisease);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            addNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ViewFavPlantActivity.this,AddNotesActivity.class);
                    intent.putExtra("name",getName);
                    startActivity(intent);
                }
            });
            viewNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ViewFavPlantActivity.this,ViewNotesActivity.class);
                    intent.putExtra("name",getName);
                    Toast.makeText(ViewFavPlantActivity.this, getName, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });
            Map<String,Object> map=new HashMap<>();
            map.put("seen","Last seen on "+seen);
            dbref.child(key).updateChildren(map);
        }

    }
}
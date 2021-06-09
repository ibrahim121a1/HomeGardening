package com.soft.homegardening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewPlantListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<ModelClass> options;
    AdaptorClass adaptorClass;
    DatabaseReference databaseReference,databaseReferenceplant,databaseReferencevegetable,databaseReferencetree,databaseReferenceherbs;
    String plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant_list);
        recyclerView=findViewById(R.id.recyclerview);
        plant=getIntent().getExtras().getString("name");
        if (plant.equals("flower"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("flower");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReference, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();
        }
        else if (plant.equals("tree")){
            databaseReferencetree= FirebaseDatabase.getInstance().getReference("tree");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReferencetree, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();
        }
        else if (plant.equals("Plant")){
            databaseReferenceplant= FirebaseDatabase.getInstance().getReference("Plant");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReferenceplant, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();
        }
        else if (plant.equals("vegetable")){
            databaseReferencevegetable= FirebaseDatabase.getInstance().getReference("vegetable");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReferencevegetable, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();
        }
        else if (plant.equals("herbs")){
            databaseReferenceherbs= FirebaseDatabase.getInstance().getReference("herbs");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReferenceherbs, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();
        }


    }
}
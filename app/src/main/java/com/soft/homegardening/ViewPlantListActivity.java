package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ViewPlantListActivity extends AppCompatActivity {
    //DECLARE VARIABLE
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<ModelClass> options;
    AdaptorClass adaptorClass;
    DatabaseReference databaseReference,databaseReferenceplant,databaseReferencevegetable,databaseReferencetree,databaseReferenceherbs;
    String plant;
    EditText searchEditText;
    ImageView searchimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant_list);
        recyclerView=findViewById(R.id.recyclerview);
        searchEditText=findViewById(R.id.searchtext);
        plant=getIntent().getExtras().getString("name");   //GET THE NAME OF PLANT CATEGORY CLICK THE USER IN PREVIOUS ACTIVITY

        databaseReference= FirebaseDatabase.getInstance().getReference("flower");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null)
                {
                    filtertext(s.toString(),plant);
                }
                else {
                    filtertext("",plant);
                }
            }
        });

        // SHOW FLOWER
        if (plant.equals("flower"))
        {

            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReference, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();

        }

        //SHOW CHILLIES
        else if (plant.equals("tree")){
            databaseReferencetree= FirebaseDatabase.getInstance().getReference("tree");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReferencetree, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();

        }
        //SHOW FRUITS
        else if (plant.equals("Plant")){
            databaseReferenceplant= FirebaseDatabase.getInstance().getReference("Plant");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReferenceplant, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();

        }

        //SHOW VEGETABLES
        else if (plant.equals("vegetable")){
            databaseReferencevegetable= FirebaseDatabase.getInstance().getReference("vegetable");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReferencevegetable, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();

        }
        //SHOW HERBS
        else if (plant.equals("herbs")){
            databaseReferenceherbs= FirebaseDatabase.getInstance().getReference("herbs");
            options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(databaseReferenceherbs, ModelClass.class).build();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adaptorClass=new AdaptorClass(options,this,plant);
            recyclerView.setAdapter(adaptorClass);
            adaptorClass.startListening();

        }


    }

    private void filtertext(String toString,String name) {
        DatabaseReference dbrefq=FirebaseDatabase.getInstance().getReference(name);
        Query query=dbrefq.orderByChild("name").startAt(toString.toUpperCase()).endAt(toString.toUpperCase()+"\uf8ff");
        options=new FirebaseRecyclerOptions.Builder<ModelClass>().setQuery(query, ModelClass.class).build();

        FirebaseRecyclerAdapter<ModelClass, AdaptorClass.ViewHolder> adapter=new FirebaseRecyclerAdapter<ModelClass, AdaptorClass.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdaptorClass.ViewHolder viewHolder, int i, @NonNull ModelClass modelClass) {
                viewHolder.plantnameTV.setText(modelClass.getName());
                Glide.with(ViewPlantListActivity.this).load(modelClass.getUrl()).into(viewHolder.plantIV);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = getRef(i).getKey();  //parent of click plant
                        String name = viewHolder.plantnameTV.getText().toString();
                        Intent intent = new Intent(ViewPlantListActivity.this, ViewDetailActivity.class); //move to view detail activity
                        intent.putExtra("key", key);
                        intent.putExtra("plant", plant);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public AdaptorClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
                AdaptorClass.ViewHolder viewHolder=new AdaptorClass.ViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
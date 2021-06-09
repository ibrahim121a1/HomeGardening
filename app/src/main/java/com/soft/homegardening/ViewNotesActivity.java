package com.soft.homegardening;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewNotesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<NotesModelClass> options;
    String name;
    FirebaseAuth firebaseAuth;
    NotesAdaptorClass notesAdaptorClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        recyclerView = findViewById(R.id.notesrv);
        name = getIntent().getStringExtra("name");
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("My Garden")
                .child(name.toLowerCase()).child("My Notes");
        options = new FirebaseRecyclerOptions.Builder<NotesModelClass>().setQuery(databaseReference, NotesModelClass.class).build();
        notesAdaptorClass = new NotesAdaptorClass(options);
        recyclerView.setAdapter(notesAdaptorClass);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdaptorClass.startListening();
    }
}
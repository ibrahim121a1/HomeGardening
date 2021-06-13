package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soft.homegardening.activities.DashBoardActivity;

import java.util.HashMap;
import java.util.Map;

public class AddNotesActivity extends AppCompatActivity {

    //declare variables
    EditText notesEt;
    Button addBtn;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        notesEt=findViewById(R.id.NotesET);
        addBtn=findViewById(R.id.addnoteslbtn);
        firebaseAuth=FirebaseAuth.getInstance();
        name= getIntent().getStringExtra("name");
        databaseReference=FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("My Garden").child(name.toLowerCase());
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notesEt.getText().toString().isEmpty())
                {
                    notesEt.setError("Please Enter Notes");
                }
                else
                {
                    //Add notes about plant
                    Map<Object,String> notesmap=new HashMap<>();
                    notesmap.put("notes",notesEt.getText().toString());
                    databaseReference.child("My Notes").push().setValue(notesmap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddNotesActivity.this, "Notes Successfully Added", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddNotesActivity.this, DashBoardActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNotesActivity.this, "Failed to add Notes", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
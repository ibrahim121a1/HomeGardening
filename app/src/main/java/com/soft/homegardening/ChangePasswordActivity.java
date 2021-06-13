package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    //declare variable
    EditText newpassword, changepassword;
    Button changePassbtn;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        //initialize variable
        newpassword = findViewById(R.id.changeET);
        changepassword = findViewById(R.id.confirmET);
        changePassbtn = findViewById(R.id.sendemailbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //change password
        changePassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newwpass=newpassword.getText().toString();
                if (!newpassword.getText().toString().isEmpty() && !changepassword.getText().toString().isEmpty()) {
                    if (newpassword.getText().toString().length() > 8) {
                        if (!newpassword.getText().toString().equals(changepassword.getText().toString())) {
                            newpassword.setError("Both are not same");
                            changepassword.setError("Both are not same");
                        } else {
                            firebaseUser.updatePassword(newwpass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChangePasswordActivity.this, "Password Successfully changes", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChangePasswordActivity.this, "Password Failed to change", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } else if (newpassword.getText().toString().isEmpty() && changepassword.getText().toString().isEmpty()) {
                    newpassword.setError("Empty");
                    changepassword.setError("Empty");
                } else if (newpassword.getText().toString().isEmpty()) {
                    newpassword.setError("Empty");
                } else if (changepassword.getText().toString().isEmpty()) {
                    changepassword.setError("Empty");
                }

            }
        });
    }
}
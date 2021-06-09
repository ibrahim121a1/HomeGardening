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

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText EmailET;
    Button sendemailbtn;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        EmailET=findViewById(R.id.EmailET);
        sendemailbtn=findViewById(R.id.sendemailbtn);
        firebaseAuth=FirebaseAuth.getInstance();
        sendemailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EmailET.getText().toString().isEmpty())
                {
                    firebaseAuth.sendPasswordResetEmail(EmailET.getText().toString()).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgetPasswordActivity.this, "Could'nt send Email", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ForgetPasswordActivity.this, "Email Send Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (EmailET.getText().toString().isEmpty())
                {
                    EmailET.setError("Enter Your Email");
                }
            }
        });
    }
}
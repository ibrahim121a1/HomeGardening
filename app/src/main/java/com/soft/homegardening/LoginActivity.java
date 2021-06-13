package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.soft.homegardening.activities.DashBoardActivity;

public class LoginActivity extends AppCompatActivity {

    //VARIABLE DECLARED
    Button signupbtn, signinbtn;
    EditText EmailET, PasswordET;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    TextView forgetpasswordTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //VARIABLE INITIALIZED
        signupbtn = findViewById(R.id.register_btn);
        EmailET = findViewById(R.id.EmailET);
        PasswordET = findViewById(R.id.passwordET);
        firebaseAuth = FirebaseAuth.getInstance();
        signinbtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progressbar);
        forgetpasswordTV = findViewById(R.id.forgetpasswordTV);
        //MOVE TO FORGET PASSWORD ACTIVITY
        forgetpasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });
        // SIGNIN AFTER AUTHENTICATION
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
        //MOVE TO SIGNUP ACTIVITY
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }


    void signin() {
        if (EmailET.getText().toString().isEmpty() && PasswordET.getText().toString().isEmpty()) {
            EmailET.setError("Please Enter Your Email");
            PasswordET.setError("Please Enter Your Password");
        } else if (EmailET.getText().toString().isEmpty()) {
            EmailET.setError("Please Enter Your Email");
        } else if (PasswordET.getText().toString().isEmpty()) {
            PasswordET.setError("Please Enter Your Password");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            signinbtn.setEnabled(false);
            firebaseAuth.signInWithEmailAndPassword(EmailET.getText().toString(), PasswordET.getText().toString()) //CREATE ACCOUNT WITH EMAIL AND PASSWORD
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                signinbtn.setEnabled(true);
                                try {
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                    if (errorCode == "ERROR_INVALID_EMAIL") {
                                        EmailET.setError("The email address is badly formatted");
                                    } else if (errorCode == "ERROR_USER_NOT_FOUND") {
                                        EmailET.setError("User Not Found");
                                    } else if (errorCode == "ERROR_WRONG_PASSWORD") {
                                        PasswordET.setError("Incorrect Password");
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(LoginActivity.this, "Unable to Login", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Sucessfully Login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
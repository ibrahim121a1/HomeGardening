package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soft.homegardening.activities.DashBoardActivity;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    //declare variable
    EditText EmailET, PasswordET, CpasswordET, UsernameET;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button signupbtn;
    ProgressBar progressBar;
    TextView alreadyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();
        // signup after authentication
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    // initialize variable
    private void initialize() {
        EmailET = findViewById(R.id.EmailET);
        PasswordET = findViewById(R.id.passwordET);
        CpasswordET = findViewById(R.id.cpasswordET);
        UsernameET = findViewById(R.id.UserNameET);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        databaseReference = firebaseDatabase.getReference("Member");
        signupbtn = findViewById(R.id.login_btn);
        alreadyTV = findViewById(R.id.alreasy_tv);
        alreadyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void signup() {
        try {
            if (!EmailET.getText().toString().isEmpty() && !PasswordET.getText().toString().isEmpty()
                    && !CpasswordET.getText().toString().isEmpty() && !UsernameET.getText().toString().isEmpty()) {
                if (PasswordET.getText().toString().length() > 8) {
                    if (!PasswordET.getText().toString().equals(CpasswordET.getText().toString())) {
                        PasswordET.setError("Both Are Not Same");
                        CpasswordET.setError("Both Are Not Same");
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        signupbtn.setEnabled(false);
                        firebaseAuth.createUserWithEmailAndPassword(EmailET.getText().toString(), PasswordET.getText().toString()) //signup with email and password
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (!task.isSuccessful()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            signupbtn.setEnabled(true);
                                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                            if (errorCode == "ERROR_INVALID_EMAIL") {
                                                EmailET.setError("The email address is badly formatted");
                                            } else if (errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                                                EmailET.setError("The email address is already in use");
                                            }

                                        } else {
                                            signupbtn.setEnabled(true);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Map<Object, String> objectStringMap = new HashMap<>();
                                            objectStringMap.put("Username", UsernameET.getText().toString());
                                            databaseReference.child(firebaseAuth.getUid()).setValue(objectStringMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Intent intent = new Intent(SignUpActivity.this, DashBoardActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUpActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUpActivity.this, "Account Not Created", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    PasswordET.setError("Length Should be Greater Than 8");
                }

            } else if (EmailET.getText().toString().isEmpty() && PasswordET.getText().toString().isEmpty() && UsernameET.getText().toString().isEmpty()
                    && CpasswordET.getText().toString().isEmpty()) {
                EmailET.setError("Enter Your Email");
                UsernameET.setError("Enter Your UserName");
                PasswordET.setError("Enter Your PAssword");
                CpasswordET.setError("Enter Your Confirm PAssword");
            } else if (EmailET.getText().toString().isEmpty()) {
                EmailET.setError("Enter Your Email");
            } else if (UsernameET.getText().toString().isEmpty()) {
                UsernameET.setError("Enter Your USerName");
            } else if (PasswordET.getText().toString().isEmpty()) {
                PasswordET.setError("Enter Your Password");
            } else if (CpasswordET.getText().toString().isEmpty()) {
                CpasswordET.setError("Enter the Confirm Password");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
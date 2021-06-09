package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SavedDataDetailActivity extends AppCompatActivity {

    EditText plantName,plantDescription;
    ImageView plantImage;
    private  static final int galleryPick = 1;
    private Uri imageUri;

    private StorageReference plantImageRef;
    private DatabaseReference plantReference;
    FirebaseAuth firebaseAuth;
    Button uploadbtn;
    String description,name,saveCurrentDate, saveCurrentTime, productRandomKey, downloadImageUrl;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_data_detail);
        plantName=findViewById(R.id.plantnameET);
        plantDescription=findViewById(R.id.plantdetailET);
        plantImage=findViewById(R.id.select_productIV);
        firebaseAuth=FirebaseAuth.getInstance();
        plantReference= FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("Save Data");
        plantImageRef= FirebaseStorage.getInstance().getReference("Member");
        uploadbtn=findViewById(R.id.uploadbtn);
        loadingBar= new ProgressDialog(this);

        plantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePlantData();
            }
        });

    }
    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/");
        startActivityForResult(galleryIntent,galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            plantImage.setImageURI(imageUri);
        }

    }
    private void validatePlantData() {
         description = plantDescription.getText().toString();
         name = plantName.getText().toString();


        if (imageUri == null) {
            Toast.makeText(this, "Plant image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please write Plant description...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write Plant name...", Toast.LENGTH_SHORT).show();
        }
        else {
            storePlantInformation();
        }
    }
    private void storePlantInformation()
    {
        loadingBar.setTitle("Add New Plant");
        loadingBar.setMessage("Please wait...");

        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calender= Calendar.getInstance();

        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        productRandomKey = saveCurrentDate+saveCurrentTime;
        final StorageReference filePath = plantImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".png");

        final UploadTask objectUploadTask = filePath.putFile(imageUri);

        objectUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(SavedDataDetailActivity.this,"Error: " + message,Toast.LENGTH_SHORT).show();

                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(SavedDataDetailActivity.this,"Product Image Uploaded Successfully!",Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(SavedDataDetailActivity.this,"Got the Product Image Url Successfully!",Toast.LENGTH_SHORT).show();

                            savePlantInfoToDB();
                        }
                    }
                });
            }
        });
    }

    private void savePlantInfoToDB()
    {
        HashMap<String,Object> objectHashMap = new HashMap<>();
        objectHashMap.put("pid",productRandomKey);

        objectHashMap.put("date",saveCurrentDate);
        objectHashMap.put("time",saveCurrentTime);

        objectHashMap.put("description",description);
        objectHashMap.put("url",downloadImageUrl);


        objectHashMap.put("name",name);
        plantReference.child(productRandomKey).setValue(objectHashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SavedDataDetailActivity.this, "Plant Added Sucessfully", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();

                        }
                        else
                        {
                            loadingBar.dismiss();

                            String message = task.getException().toString();
                            Toast.makeText(SavedDataDetailActivity.this,"Error: " + message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
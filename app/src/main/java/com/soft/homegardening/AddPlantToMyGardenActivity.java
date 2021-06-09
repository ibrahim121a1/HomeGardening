package com.soft.homegardening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soft.homegardening.activities.DashBoardActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPlantToMyGardenActivity extends AppCompatActivity {

    EditText plantNameET, timeET, dateET;
    Button addMygardenBtn;
    ImageView plantIV;
    String description, name, saveCurrentDate, saveCurrentTime, productRandomKey, downloadImageUrl, time, date
            ,getprepare,getplant,getCare,getFetilizer,getdisease,geturl,getplantName;
    private ProgressDialog loadingBar;
    private Uri imageUri;
    private StorageReference plantImageRef;
    private DatabaseReference plantReference,checkplantRef;
    FirebaseAuth firebaseAuth;
    Calendar calender;
    private static final int GALLERY_REQUEST = 1;

    private static final int CAMERA_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant_to_my_garden);
        plantIV = findViewById(R.id.select_productIV);
        plantNameET = findViewById(R.id.plantnameET);
        timeET = findViewById(R.id.timeET);
        dateET = findViewById(R.id.dateET);
        calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());
        timeET.setText(saveCurrentTime);
        dateET.setText(saveCurrentDate);
        checkplantRef=FirebaseDatabase.getInstance().getReference("All Plants");

        plantIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        plantReference = FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid()).child("My Garden");
        plantImageRef = FirebaseStorage.getInstance().getReference("Member");
        addMygardenBtn = findViewById(R.id.uploadbtn);
        addMygardenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateplantData();


            }
        });

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your Plant picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), selectedImage, "Title", null);
                        imageUri = Uri.parse(path);
                        plantIV.setImageURI(imageUri);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        imageUri = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (imageUri != null) {
                            Cursor cursor = getContentResolver().query(imageUri,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
//                                plantIV.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                plantIV.setImageURI(imageUri);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
//
        }
    }

    private void validateplantData() {
        time = timeET.getText().toString();
        name = plantNameET.getText().toString();
        date = dateET.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Plant image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(time)) {
            Toast.makeText(this, "Please write time", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write Plant name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please write date name...", Toast.LENGTH_SHORT).show();
        } else {
            checkplantRef.child(plantNameET.getText().toString().toLowerCase()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {

                        getplantName=snapshot.child("name").getValue(String.class);
                        geturl=snapshot.child("url").getValue(String.class);
                        getCare=snapshot.child("care").getValue(String.class);
                        getFetilizer=snapshot.child("fertilizer").getValue(String.class);
                        getprepare=snapshot.child("preparing").getValue(String.class);
                        getplant=snapshot.child("planting").getValue(String.class);
                        getdisease=snapshot.child("disease").getValue(String.class);
                        storePlantInformation();
                        Toast.makeText(AddPlantToMyGardenActivity.this,"Plant Found :-)"+ name, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AddPlantToMyGardenActivity.this, "Plant Not Found :-(", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void storePlantInformation() {
        loadingBar.setTitle("Add New Plant To My Garden");
        loadingBar.setMessage("Please wait...");

        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        productRandomKey = saveCurrentDate + saveCurrentTime;
        final StorageReference filePath = plantImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".png");

        final UploadTask objectUploadTask = filePath.putFile(imageUri);

        objectUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddPlantToMyGardenActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddPlantToMyGardenActivity.this, "Plant Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AddPlantToMyGardenActivity.this, "Got the Plant Image Url Successfully!", Toast.LENGTH_SHORT).show();

                            savePlantInfoToDB();
                        }
                    }
                });
            }
        });
    }

    private void savePlantInfoToDB() {
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("pid", productRandomKey);

        objectHashMap.put("date", saveCurrentDate);
        objectHashMap.put("time", saveCurrentTime);

        objectHashMap.put("description", description);
        objectHashMap.put("url", downloadImageUrl);
        objectHashMap.put("planting",getplant);
        objectHashMap.put("preparing",getprepare);
        objectHashMap.put("care",getCare);
        objectHashMap.put("disease",getdisease);
        objectHashMap.put("fertilizer",getFetilizer);
        objectHashMap.put("seen","");
        objectHashMap.put("name", getplantName);
        plantReference.child(name).setValue(objectHashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddPlantToMyGardenActivity.this, "Plant Added Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddPlantToMyGardenActivity.this, DashBoardActivity.class));

                            loadingBar.dismiss();

                        } else {
                            loadingBar.dismiss();

                            String message = task.getException().toString();
                            Toast.makeText(AddPlantToMyGardenActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
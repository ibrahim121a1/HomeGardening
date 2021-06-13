package com.soft.homegardening;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.soft.homegardening.activities.DashBoardActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AddNewAlarmActivity extends AppCompatActivity {

    //declare variable
    Button setalarm, cancelalarm;
    private int notificationId = 1;
    TimePicker timePicker;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    int hour, minute;
    EditText label;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Calendar dayinweek;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);
        //initialize
        setalarm = findViewById(R.id.set_alarm);
        cancelalarm = findViewById(R.id.cancel_alarm);
        timePicker = findViewById(R.id.timePicker);
        label = findViewById(R.id.resonET);
        firebaseAuth = FirebaseAuth.getInstance();
        dayinweek = Calendar.getInstance();
        day = dayinweek.get(Calendar.DAY_OF_WEEK);
        databaseReference = FirebaseDatabase.getInstance().getReference("Member").child(firebaseAuth.getUid());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm ");
        String currentDateandTime = sdf.format(new Date());

        //set alarm
        setalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (label.getText().toString().isEmpty()) {
                    label.setError("Please Enter Your Reason to Set Alarm");
                } else {
                    Intent intent = new Intent(AddNewAlarmActivity.this, AlarmReciever.class);
                    intent.putExtra("notificationId", notificationId);  //send notification id to alarm reciever class
                    intent.putExtra("message", label.getText().toString());//send reason to alarm reciever class
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
//                    String time = hour + ":" + minute;
                    Map<Object, String> map = new HashMap<>();

                    map.put("time", hour + ":" + minute);
                    map.put("label", label.getText().toString());
                    databaseReference.child("Alarm").push().setValue(map);


                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    pendingIntent = PendingIntent.getBroadcast(AddNewAlarmActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

                    Toast.makeText(AddNewAlarmActivity.this, "Set Alarm!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddNewAlarmActivity.this, DashBoardActivity.class));
                }


            }
        });
        cancelalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.cancel(pendingIntent);

                Toast.makeText(AddNewAlarmActivity.this, "Cancel", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
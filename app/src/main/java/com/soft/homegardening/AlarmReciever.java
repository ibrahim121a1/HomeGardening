package com.soft.homegardening;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

public class AlarmReciever  extends WakefulBroadcastReceiver {
    private static final String CHANNEL_ID = "CHANNEL_SAMPLE";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get id & message
        int notificationId = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra("message");

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                AlarmService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
//
//        setResultCode(Activity.RESULT_OK);
        // Call MainActivity when notification is tapped.
        Intent mainIntent = new Intent(context, AddNewAlarmActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        // NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For API 26 and above
            CharSequence channelName = "My Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Prepare Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Alarm")
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(alarmUri)
                .setAutoCancel(true);

        // Notify
        notificationManager.notify(notificationId, builder.build());





    }




}

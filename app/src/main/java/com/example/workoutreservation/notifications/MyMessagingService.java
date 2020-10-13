package com.example.workoutreservation.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.workoutreservation.MainActivity;
import com.example.workoutreservation.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    private String ADMIN_CHANEL_ID = "admin chanel ID";

    public MyMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (remoteMessage.getData().size() > 0) {
            Log.d("MyMessagingService", "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("MyMessagingService", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        String notificationBody = remoteMessage.getNotification().getBody();
        String notificationTitle = remoteMessage.getNotification().getTitle();
//        showNotification(remoteMessage.getNotification().getBody());
        showNotification(notificationTitle, notificationBody);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        String adminChannelName = "Workout Reservation Reminders";
        String adminChannelDescription = "Notifications about upcomming workouts you are signed up";

        NotificationChannel adminChannel = new NotificationChannel(ADMIN_CHANEL_ID, adminChannelName, NotificationManager.IMPORTANCE_DEFAULT);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        notificationManager.createNotificationChannel(adminChannel);

    }

    private void showNotification(String msgTitle, String msgBody) {
//        android.os.Debug.waitForDebugger();

        String extra="";
        if(msgTitle.equals("Workout reservation availabe")){
            extra="MyWaitlists";
        }
        if(msgTitle.equals("Upcoming workout reminder")){
            extra="MyWorkouts";
        }

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("fragment", extra);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent,  PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANEL_ID);
        Log.d("MyMessagingService", msgBody);
        notificationBuilder.setSmallIcon(R.drawable.ic_schedule)
                .setContentTitle(msgTitle)
                .setContentText(msgBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTimeoutAfter(15 * 60 * 1000)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msgBody))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}

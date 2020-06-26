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
//FirebaseInstanceId.getInstance().getInstanceId();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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
        Intent intent = new Intent(this, MainActivity.class);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addNextIntentWithParentStack(intent);

        intent.putExtra("fragment", "MyWaitlists");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANEL_ID);
        Log.d("MyMessagingService", msgBody);
        notificationBuilder.setSmallIcon(R.drawable.ic_schedule)
                .setContentTitle(msgTitle)
                .setContentText(msgBody)
                .setAutoCancel(false)
//                .setUsesChronometer(true)
//                .setChronometerCountDown(true)
                .setContentIntent(pendingIntent)
                .setTimeoutAfter(15 * 60 * 1000)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msgBody))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}

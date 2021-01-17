package com.example.webviewtemplate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sPref = getSharedPreferences("AppDB", MODE_PRIVATE);
        int notificationChannel = sPref.getInt("NOTIFICATION_CHANNEL", 0);
        if(notificationChannel == 0) createNotificationChannel();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String description = data.get("description");
        createNotification(title, description);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        FirebaseMessaging.getInstance().subscribeToTopic("all_device");
    }

    private void createNotification(String title, String description) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"1")
                .setSmallIcon(R.drawable.ic_action_notification)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description));

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= 26){
            CharSequence name = "Уведомления";
            String description = "Уведомления о новых сообщениях в чате";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        SharedPreferences sPref = getSharedPreferences("AppDB", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("NOTIFICATION_CHANNEL", 1);
        ed.apply();
    }
}

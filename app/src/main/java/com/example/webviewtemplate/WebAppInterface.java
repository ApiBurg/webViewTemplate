package com.example.webviewtemplate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

class WebAppInterface {
    Context context1;
    private static final int NOTIFY_ID=101;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        context1 = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        //Toast.makeText(context1, " ", Toast.LENGTH_SHORT).show();
        //goPush();
        //sendNotification("message");
    }

    public void goPush(){
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";
        Intent notificationIntent = new Intent(context1, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent = PendingIntent.getActivity(context1,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);//FLAG_CANCEL_CURRENT

        Resources res = context1.getResources();
        // до версии Android 8.0 API 26
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context1);
        builder.setContentIntent(contentIntent)
                // обязательные настройки
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Уведомление")
                .setContentText("У вас новый запрос!") // Текст уведомления
                // необязательные настройки
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher)) // большая картинка
                .setTicker("Новый запрос!")// текст в строке состояния
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true); // автоматически закрыть уведомление после нажатия


        NotificationManager notificationManager =
                (NotificationManager) context1.getSystemService(Context.NOTIFICATION_SERVICE);
        // Альтернативный вариант
        NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(context1);
        notificationManager2.notify(NOTIFY_ID, builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotification("У вас новый запрос");// здесь сообщение для андроид 8 и выше

        }
    }

    private void sendNotification(String messageBody) {//метод вызова уведомлений для андроид 8 и выше
        Intent intent = new Intent(context1, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context1, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = context1.getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context1, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Уведомление")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context1.getSystemService(Context.NOTIFICATION_SERVICE);

        // Так как андроид Oreo и выше нужен канал уведомлений.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        assert notificationManager != null;
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

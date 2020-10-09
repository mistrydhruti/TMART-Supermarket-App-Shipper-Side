package com.example.qrcodescanner.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.example.qrcodescanner.NewOrders;
import com.example.qrcodescanner.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(remoteMessage);
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        Intent i = new Intent(MyFirebaseMessaging.this, NewOrders.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultsoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.tmart)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultsoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager noti=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(0,builder.build());

    }
}

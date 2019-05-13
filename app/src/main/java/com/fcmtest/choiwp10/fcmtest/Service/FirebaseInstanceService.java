package com.fcmtest.choiwp10.fcmtest.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fcmtest.choiwp10.fcmtest.R;
import com.fcmtest.choiwp10.fcmtest.Util.HttpRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.telephony.TelephonyManager;

public class FirebaseInstanceService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseInstanceService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        
        if (remoteMessage.getData().isEmpty()) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else {
            showNotification(remoteMessage.getData());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, s);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        JSONObject json = new JSONObject();
        try {
            json.put("uuid", uuid);
            json.put("registerKey", s);
            json.put("platform", "android");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d(TAG, json.toString());

        HttpRequest.getInstance(getApplicationContext()).httpPost("http://192.168.1.135:3000" + "/api/devices", json, new HttpRequest.HttpResponseCallback() {
            @Override
            public void onCallback(Boolean success, JSONObject jsonData) {
                Log.d(TAG, jsonData.toString());
            }
        });
    }

    private void showNotification(String title, String body) {
        setNotificationContent(title, body);
    }

    private void showNotification(Map<String,String> data) {
        String title = data.get("title").toString();
        String body = data.get("body").toString();
        setNotificationContent(title, body);
    }

    private void setNotificationContent(String title, String body) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.fcmtest.choiwp10.fcmtest";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("TEST Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[] {0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_noticifation)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }
}



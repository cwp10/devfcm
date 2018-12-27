package com.fcmtest.choiwp10.fcmtest.Service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseInstanceService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseInstanceService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d(TAG, s);
    }
}



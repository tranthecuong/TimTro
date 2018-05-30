package com.example.cuongtran.timtro.services;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.cuongtran.timtro.view.activity.MainActivity;
import com.example.cuongtran.timtro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "CUONG";
    String CHANNEL_ID = "13231";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            if (remoteMessage.getData().size() > 0) {
                // Nhan ve du lieu
                Log.e(TAG, "Message data payload: " + remoteMessage.getData());
                Map<String,String> map = remoteMessage.getData();
                String idTK = map.get("idtaikhoan");
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(idTK.equals(currentUser)){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.home_icon)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText("Đã tìm thấy nhà trọ thỏa mãn")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent);

                    // notificationId is a unique int for each notification that you must define
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
                }

            }



        }
    }
}

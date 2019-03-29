package com.example.admin.noticeapp2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotifyService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID ="123" ;
    DatabaseReference mRef;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public NotifyService() {
        super();
    }
    @Override
    public void onCreate() {
        super.onCreate();
      //  Toast.makeText(getApplicationContext(),"inService",Toast.LENGTH_SHORT).show();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mRef = FirebaseDatabase.getInstance().getReference("Notices");
       // Toast.makeText(NotifyService.this,"Service Started",Toast.LENGTH_LONG).show();

        mRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               // Toast.makeText(getApplicationContext(),"childedAdded",Toast.LENGTH_SHORT).show();
                Notice n = dataSnapshot.getValue(Notice.class);

                Log.v("DATA:   ",n.getDescrp()+"   "+n.getTitle());

                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotifyService.this)
                        .setSmallIcon(R.drawable.ic_menu_share)
                        .setContentTitle(n.getTitle())
                        .setContentText(n.getDescrp())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);


                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    assert notificationManager != null;
                    builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                notificationManager.notify(0,builder.build());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getApplicationContext(),"childChanged",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(),"ChildRemoved",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getApplicationContext(),"ChildeMoved",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"onCancelled",Toast.LENGTH_SHORT).show();
            }
        });



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

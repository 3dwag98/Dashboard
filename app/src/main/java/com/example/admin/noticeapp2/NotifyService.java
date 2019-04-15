package com.example.admin.noticeapp2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;

public class NotifyService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID ="123" ;
    DatabaseReference mRef;
    Context context;


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
        Toast.makeText(getApplicationContext(),"inService",Toast.LENGTH_SHORT).show();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Query ref = FirebaseDatabase.getInstance().getReference("Notices").orderByChild("time/time").limitToLast(1);
        mRef = FirebaseDatabase.getInstance().getReference("Notices");
        Toast.makeText(NotifyService.this,"Service Started",Toast.LENGTH_LONG).show();
        this.context = NotifyService.this;
        ref.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getApplicationContext(),"childedAdded",Toast.LENGTH_SHORT).show();
                Notice n = dataSnapshot.getValue(Notice.class);
                Intent i = new Intent(getApplicationContext(),UserNotice.class);
                PendingIntent pi =   PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);
                Log.e("DATA:   ","intent created");
                String channel_id = createNotificationChannel(context);
                Log.e("DATA:   ","channel id created");
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channel_id)
                            .setContentTitle(n.getTitle())
                            .setContentText(n.getDescrp())
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(n.getDescrp()))
                            /*.setLargeIcon(largeIcon)*/
                            .setSmallIcon(R.drawable.notice) //needs white icon with transparent BG (For all platforms)
                            .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                            .setVibrate(new long[]{1000, 1000})
                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                            .setContentIntent(pi)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setAutoCancel(true);
                    if (n.getUpload() != null && !n.getUpload().isEmpty() && !n.getUpload().equals("")) {
                        Intent intentAction = new Intent(context, ActionReceiver.class);
                        intentAction.putExtra("file", n.getUpload());
                        intentAction.putExtra("name", n.getTitle());
                        PendingIntent pIntentlogin = PendingIntent.getBroadcast(context, 1, intentAction, PendingIntent.FLAG_UPDATE_CURRENT);
                        notificationBuilder.addAction(R.drawable.buttoncolor, "DOWNLOAD", pIntentlogin);
                    }
                    Log.e("DATA:   ", "builder created");
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify((int) ((new Date(System.currentTimeMillis()).getTime() / 1000L) % Integer.MAX_VALUE) /* ID of notification */, notificationBuilder.build());
                    Log.e("DATA:   ", "Notified ");
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = "Channel_id";

            // The user-visible name of the channel.
            CharSequence channelName = "Application_name";
            // The user-visible description of the channel.
            String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            boolean channelEnableVibrate = true;
//            int channelLockscreenVisibility = Notification.;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
//            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
            Log.e("DATA:   ","Channeel created");
            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

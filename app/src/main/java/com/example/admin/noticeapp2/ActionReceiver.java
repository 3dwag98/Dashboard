package com.example.admin.noticeapp2;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getStringExtra("file");
        String name=intent.getStringExtra("name");
        int id = 0;
        intent.getIntExtra("id",id);
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "wallpaper");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
        String dir = mediaStorageDir.getAbsolutePath();
        new FileOP(context).download(context,name,"",dir,action);
        //This is used to close the notification tray
//        NotificationManager nm=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.cancel(id);
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

}

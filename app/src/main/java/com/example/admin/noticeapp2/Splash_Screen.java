package com.example.admin.noticeapp2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        Thread mythread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    ConnectivityManager connec =
                            (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

                    // Check for network connections
                    if(!( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                            connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) )
                    {
                        Intent intent = new Intent(getApplicationContext(),NetworkConnection.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                    Intent intent = new Intent(getApplicationContext(),Login_Window.class);
                    startActivity(intent);
                    finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mythread.start();
    }
}

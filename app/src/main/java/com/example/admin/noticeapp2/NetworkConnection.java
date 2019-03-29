package com.example.admin.noticeapp2;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class NetworkConnection extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_layout);

        Glide.with(NetworkConnection.this)
                .load(R.drawable.network)
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) findViewById(R.id.imageView4));


        findViewById(R.id.button2)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConnectivityManager connec =
                                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
                        if(( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) )
                        {
                            Intent intent = new Intent(getApplicationContext(),Login_Window.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(NetworkConnection.this,"No Connection",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}

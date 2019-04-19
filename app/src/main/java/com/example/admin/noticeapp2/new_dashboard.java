package com.example.admin.noticeapp2;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class new_dashboard extends AppCompatActivity {
    private ImageButton imgIcon,imgNotice,imgAbout,imgHelp,imgForum;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private BroadcastReceiver mNetworkReceiver;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_forum:
                finish();
                startActivity(new Intent(new_dashboard.this,UserForum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_notices:
                finish();
                startActivity(new Intent(new_dashboard.this,UserNotice.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_feedback:
                finish();
                startActivity(new Intent(new_dashboard.this,Help_page.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_response:
                finish();
                startActivity(new Intent(new_dashboard.this,UserResponse.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.action_logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(new_dashboard.this,Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        imgIcon = findViewById(R.id.imgIcon);
        imgNotice = findViewById(R.id.imgAddNotice);
        imgAbout = findViewById(R.id.imgAbout);
        imgHelp = findViewById(R.id.imgHelp);
        imgForum = findViewById(R.id.imgResponse);





        if(!isServiceRunning(NotifyService.class)) {
            startService(new Intent(getBaseContext(), NotifyService.class));
        }
        else{
            Toast.makeText(new_dashboard.this,"Service Running",Toast.LENGTH_LONG ).show();
        }
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user.getPhotoUrl()!= null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgIcon);
        }

        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(new_dashboard.this,account_setup_profile.class));
                DialogProfile obj = new DialogProfile();
                FragmentManager fm = getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString("file",user.getPhotoUrl().toString());
                args.putString("name",user.getDisplayName());
                args.putString("email",user.getEmail());
                obj.setArguments(args);
                obj.show(fm,"Profile");
                stopService(new Intent(getBaseContext(),NotifyService.class));

            }
        });

        imgNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"View Notice module required",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(new_dashboard.this, UserNotice.class));
            }
        });

        imgForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Forum module required",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext() ,UserForum.class ));
            }
        });

        imgAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() ,UserResponse.class ));
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() ,Help_page.class ));
            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setTitle("Close Application");
        builder.setMessage("Do you want Exit ? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

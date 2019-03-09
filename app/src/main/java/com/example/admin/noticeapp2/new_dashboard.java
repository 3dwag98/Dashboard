package com.example.admin.noticeapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class new_dashboard extends AppCompatActivity {
    private ImageButton imgIcon,imgNotice,imgAbout,imgHelp,imgForum;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        imgIcon = findViewById(R.id.imgIcon);
        imgNotice = findViewById(R.id.imgAddNotice);
        imgAbout = findViewById(R.id.imgAbout);
        imgHelp = findViewById(R.id.imgHelp);
        imgForum = findViewById(R.id.imgResponse);

        imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Menu module required",Toast.LENGTH_SHORT).show();
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                finish();
                startActivity(new Intent(new_dashboard.this,Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        imgNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"View Notice module required",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(new_dashboard.this,CardDemoActivity.class));
            }
        });

        imgForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Forum module required",Toast.LENGTH_SHORT).show();
            }
        });

        imgAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() ,AboutUs.class ));
            }
        });

        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() ,Help_page.class ));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

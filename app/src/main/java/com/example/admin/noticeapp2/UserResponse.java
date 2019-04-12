package com.example.admin.noticeapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserResponse extends AppCompatActivity {

    private EditText txtResponse;
    private Button btnSubmit;
    private TextView txtQuery;
    private ImageButton imgBack;
    private AppCompatSpinner Tospin;
    String[] Tolist = {"Admin","Member1","Member2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_response);

        txtQuery = findViewById(R.id.txtQuery);
        txtResponse = findViewById(R.id.txtResponse);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgBack = findViewById(R.id.imgBack);

        Tospin = findViewById(R.id.toList);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(UserResponse.this,
                android.R.layout.simple_spinner_item, Tolist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tospin.setAdapter(adapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),admin_dashboard.class));
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submit();
            }
        });

    }

    private void Submit() {
        // reply response
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),admin_dashboard.class));
    }
}

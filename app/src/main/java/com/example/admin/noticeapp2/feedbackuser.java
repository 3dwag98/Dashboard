package com.example.admin.noticeapp2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;


public class feedbackuser extends AppCompatActivity {

    private EditText txtQuery;
    private Button btnSubmit;
   // private DatabaseReference db;
 //private FirebaseDatabase database ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackuser);

        txtQuery = findViewById(R.id.txtQuery);
        btnSubmit = findViewById(R.id.btnSubmit);


    }
}

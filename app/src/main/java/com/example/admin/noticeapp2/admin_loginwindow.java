package com.example.admin.noticeapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class admin_loginwindow extends AppCompatActivity {

    private EditText txtUname,txtPass;
    private Button btnSign;
    private ImageButton imgBack;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_window);
        mAuth =  FirebaseAuth.getInstance();

        txtPass = findViewById(R.id.txtPass);
        txtUname = findViewById(R.id.txtUname);
        btnSign = findViewById(R.id.btnSignin);
        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser()!=null) {
                    mAuth.signOut();
                }
                startActivity(new Intent(getApplicationContext(),Login_Window.class));
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signin();
            }
        });

    }



    private void Signin() {

        //Signin Request
        String email = txtUname.getText().toString();
        String pass = txtPass.getText().toString();
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(),admin_dashboard.class));
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();


        //firebase Signin Code

    }

    @Override
    public void onBackPressed() {
        if(mAuth.getCurrentUser()!=null) {
            mAuth.signOut();
        }
        startActivity(new Intent(getApplicationContext(),Login_Window.class));
    }
}

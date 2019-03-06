package com.example.admin.noticeapp2;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Window extends AppCompatActivity {

    private Button btnLogin,btnSignup;
    private EditText txtUname , txtPass;
    private Switch switchAdmin;
    private ImageButton imgBack;
    private CheckBox checkRem;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_window);

        if(isLoggedIn()){
            startActivity(new Intent(getApplicationContext(),new_dashboard.class));
        }

        final String TAG = "Dashboard";
        txtUname = findViewById(R.id.txtUsername);
        txtPass =  findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        imgBack = findViewById(R.id.imgBack);
        switchAdmin = findViewById(R.id.switchAdmin);
        checkRem = findViewById(R.id.checkRemeber);

        switchAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    startActivity(new Intent(getApplicationContext(),admin_loginwindow.class));
                }
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
               finishAffinity();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Registration.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    CheckLogin();

            }
        });

    }

    private boolean isLoggedIn() {
        //isLogged code
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            finish();
            return true;
        }
        return false;
    }

    private void CheckLogin(){

        if(TextUtils.isEmpty(txtUname.getText())){
            txtUname.setError("Field is required");

        }
        if(TextUtils.isEmpty(txtPass.getText())){
            txtPass.setError("Field is required");
        }



        //firebase login check
        Toast.makeText(Login_Window.this,"Login triggered",Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(Login_Window.this,new_dashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        //checkRem.setText(email[0]);
        //txtUname.setText();
        String email = txtUname.getText().toString();
        String pass = txtPass.getText().toString();
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.v("Dash",e.getMessage());
                        }
                    })
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login_Window.this, "sign-in success..", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login_Window.this, new_dashboard.class));
                            } else {
                                Toast.makeText(Login_Window.this, "sign-in failed..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

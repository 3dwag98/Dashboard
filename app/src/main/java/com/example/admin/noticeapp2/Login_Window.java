package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private CheckBox checkRem;
    Validate v;
    private FirebaseAuth mAuth;
    private SharedPreferences loginPreferences,memberlogin;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    SharedPreferences.Editor memberEditor;
    boolean ismember;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_window);

//        if(isLoggedIn()){
//            startActivity(new Intent(getApplicationContext(),new_dashboard.class));
//        }
        isLoggedIn();
        final String TAG = "Dashboard";
        txtUname = findViewById(R.id.txtUsername);
        txtPass =  findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        switchAdmin = findViewById(R.id.switchAdmin);
        checkRem = findViewById(R.id.checkRemeber);


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txtUname.setText(loginPreferences.getString("username", ""));
            txtPass.setText(loginPreferences.getString("password", ""));
            checkRem.setChecked(true);
        }
        v = new Validate();

        txtUname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(v.isValidEmail(txtUname)){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        switchAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    startActivity(new Intent(getApplicationContext(),admin_loginwindow.class));
                }
            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),account_setup_profile.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pg= new ProgressDialog(Login_Window.this);
                pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pg.setTitle("Log-IN Process");
                pg.setMessage("In Progress");
                pg.show();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Member");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean haschild = false;
                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(child.getValue().equals(txtUname.getText().toString())){
                                haschild = true;
                            }
                        }
                        if(haschild){
                            MemberLogin();
                        }else{
                            CheckLogin();
                            Toast.makeText(Login_Window.this,"User Login",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    private void MemberLogin() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            pg.dismiss();
            finish();
            startActivity(new Intent(Login_Window.this,Forum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        final String email = txtUname.getText().toString();
        final String pass = txtPass.getText().toString();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txtUname.getWindowToken(), 0);

                        if (checkRem.isChecked()) {
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putString("username", email);
                            loginPrefsEditor.putString("password", pass);
                            loginPrefsEditor.commit();
                            memberEditor.putBoolean("saveLogin", true);
                            memberEditor.commit();
                        } else {
                            loginPrefsEditor.clear();
                            loginPrefsEditor.commit();
                            memberEditor.clear();
                            memberEditor.commit();
                        }
                        Toast.makeText(Login_Window.this,"WELCOME",Toast.LENGTH_LONG);
                    }
                })
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
                            pg.dismiss();
                            Toast.makeText(Login_Window.this, "sign-in success..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login_Window.this, Forum.class));
                        } else {
                            Toast.makeText(Login_Window.this, "sign-in failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void isLoggedIn() {
        //isLogged code
        mAuth = FirebaseAuth.getInstance();
        memberlogin = getSharedPreferences("memberPref", MODE_PRIVATE);
         memberEditor = memberlogin.edit();
         ismember = memberlogin.getBoolean("saveLogin", false);

        if(mAuth.getCurrentUser() != null){
            if(ismember){
                finish();
                startActivity(new Intent(Login_Window.this, Forum.class));
            }
            else{
                finish();
                startActivity(new Intent(Login_Window.this, new_dashboard.class));
            }
        }

    }

    private void CheckLogin(){

        if(TextUtils.isEmpty(txtUname.getText())){
            txtUname.setError("Field is required");

        }
        if(TextUtils.isEmpty(txtPass.getText())){
            txtPass.setError("Field is required");
        }



        //firebase login check
      //  Toast.makeText(Login_Window.this,"Login triggered",Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            pg.dismiss();
            finish();
            startActivity(new Intent(Login_Window.this,new_dashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }


        //checkRem.setText(email[0]);
        //txtUname.setText();
        final String email = txtUname.getText().toString();
        final String pass = txtPass.getText().toString();
            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(txtUname.getWindowToken(), 0);

                            if (checkRem.isChecked()) {
                                loginPrefsEditor.putBoolean("saveLogin", true);
                                loginPrefsEditor.putString("username", email);
                                loginPrefsEditor.putString("password", pass);
                                loginPrefsEditor.commit();
                            } else {
                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                            }
                            Toast.makeText(Login_Window.this,"WELCOME",Toast.LENGTH_LONG);
                        }
                    })
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
                                pg.dismiss();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
}

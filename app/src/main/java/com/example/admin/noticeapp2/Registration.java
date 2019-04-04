package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Registration extends AppCompatActivity {

    private EditText txtUname;
    private EditText txtFname;
    private EditText txtLname;
    private EditText txtMnum;
    private EditText txtEmail;
    private EditText txtPass;
    private Button btnRegister;
    private ImageButton btnBack;
    Validate v = new Validate();
    View vw;
    ProgressDialog pg;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        txtFname = findViewById(R.id.fname);
        txtLname = findViewById(R.id.lname);
        txtUname = findViewById(R.id.uname);
        txtMnum = findViewById(R.id.mobnum);
        txtEmail = findViewById(R.id.email);
        txtPass = findViewById(R.id.pass);
        vw = findViewById(R.id.view_reg);

        txtFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(v.isValidString(txtFname)){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtLname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(v.isValidString(txtLname)){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if(v.isValidEmail(txtEmail)){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(v.isValidPassword(txtPass)){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        txtMnum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txtMnum.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }
                if(v.isValidNumber(txtMnum)){}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        btnRegister = findViewById(R.id.register);
        btnBack = findViewById(R.id.backbtn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(v.isValidEmail(txtEmail) && v.isValidPassword(txtPass)) {
                    pg = new ProgressDialog(Registration.this);
                    pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pg.setTitle("Register Process");
                    pg.setMessage("In Progress");
                    pg.show();
                    Register();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login_Window.class));
            }
        });
    }

    private void Register() {
        mAuth = FirebaseAuth.getInstance();
        final String fname,uname,lname,email,pass,mobile_no;
        fname = txtFname.getText().toString();
        lname = txtLname.getText().toString();
        uname = txtUname.getText().toString();
        email = txtEmail.getText().toString();
        pass = txtPass.getText().toString();
        mobile_no = txtMnum.getText().toString().trim();
        final Student obj = new Student(fname,lname,uname,email,mobile_no);



        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Students");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(fname.trim())){
                    if(dataSnapshot.child(fname.trim()).getValue().equals(email.trim())){
                        reg(uname,email,pass,obj);
                    }
                    else{
                        pg.dismiss();
                        Toast.makeText(Registration.this,"Enter Valid Email-Id",Toast.LENGTH_LONG).show();
                    }
                }else{
                    pg.dismiss();
                    Toast.makeText(Registration.this,"Not a Valid Student Or Name is Invalid",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Registration.this,databaseError.toString(),Toast.LENGTH_LONG).show();

            }
        });

    }

    private void reset() {
        txtUname.setText(null);
        txtFname.setText(null);
        txtLname.setText(null);
        txtMnum.setText(null);
        txtEmail.setText(null);
        txtPass.setText(null);
    }



    private void reg(final String uname, String email, String pass, final Student obj) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            pg.dismiss();
                            Snackbar.make(vw, "   Sign-up success..   ",Snackbar.LENGTH_LONG).show();
                            reset();
                            //Toast.makeText(Registration.this, "Sign-up success..", Toast.LENGTH_SHORT).show();
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                            db.child(uname).setValue(obj);

                        } else {
                            Toast.makeText(Registration.this, "Sign-up failed..", Toast.LENGTH_SHORT).show();
                        }
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(Registration.this,"User Already Exists!!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(),Login_Window.class));
    }
}

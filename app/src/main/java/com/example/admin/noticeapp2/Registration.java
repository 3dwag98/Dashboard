package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Registration extends AppCompatActivity {

    private EditText txtUname;
    private EditText txtFname;
    private EditText txtLname;
    private EditText txtMnum;
    private EditText txtEmail;
    private EditText txtPass;
    private Button btnRegister;
    private ImageButton btnBack;
    private TextInputLayout errUname,errFname,errLname,errMnum,errEmail,errPass;
    Validate v = new Validate();
    View vw;
    ProgressDialog pg;
    String Class;
    Uri filePath;
    private FirebaseAuth mAuth;
    FirebaseUser user;

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

        errUname= findViewById(R.id.unameInputText);
        errFname= findViewById(R.id.fnameInputText);
        errLname= findViewById(R.id.lnameInputText);
        errMnum= findViewById(R.id.mnumInputText);
        errEmail= findViewById(R.id.emailInputText);
        errPass= findViewById(R.id.passInputText);

        Intent i = getIntent();
        Class = i.getStringExtra("class");
        if(i.getStringExtra("filePath")!= null) {
            filePath = Uri.parse(i.getStringExtra("filePath"));
        }else{
            filePath = null;
        }

        txtFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(v.isValidString(txtFname,errFname)){
                    errFname.setError(null);
                }
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
                if(v.isValidString(txtLname,errLname)){
                    errLname.setError(null);
                }
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
               if(v.isValidEmail(txtEmail,errEmail)){
                   errEmail.setError(null);
               }
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
                if(v.isValidPassword(txtPass,errPass)){
                    errPass.setError(null);
                }
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
                if(v.isValidNumber(txtMnum,errMnum)){
                    errMnum.setError(null);
                }
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
                startActivity(new Intent(getApplicationContext(),account_setup_profile.class));
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


        final Student obj = new Student(fname,lname,uname,email,Class,null,mobile_no);



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
                        Toast.makeText(Registration.this,"Enter Valid Email-Id,You're Email is not Registered",Toast.LENGTH_LONG).show();
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
                            Snackbar.make(vw, "   Sign-up success..   ",Snackbar.LENGTH_LONG).show();
                            //reset();
                            user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(uname)
                                    .build();
                            user.updateProfile(profileUpdates);
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                            db.child(uname).setValue(obj);
                            if(filePath!=null){
                                updateCreds(uname,filePath);
                            }
                            pg.dismiss();
                        } else {
                            Toast.makeText(Registration.this, "Sign-up failed..", Toast.LENGTH_SHORT).show();
                            pg.dismiss();
                        }
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(Registration.this,"User Already Exists!!",Toast.LENGTH_SHORT).show();
                            pg.dismiss();
                        }

                    }
                });
    }

    private void updateCreds(final String uname, Uri filePath) {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference mfile= mStorageRef.child("UserUploads").child(uname);
        mfile.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();
                        user.updateProfile(profileUpdates);
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                        db.child(uname).child("profile_pic").setValue(uri.toString());
                    }
                });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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

package com.example.admin.noticeapp2;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddMember extends AppCompatActivity implements RegisterDialog.DialogListener{
    EditText txtName;
    String email,pass,name;
    ListView lv;
    List member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        txtName = findViewById(R.id.name);
        lv = findViewById(R.id.lv);
        member = new ArrayList<String>();
        ArrayAdapter<String> ad = new ArrayAdapter(AddMember.this,android.R.layout.simple_list_item_1,member);
        lv.setAdapter(ad);
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Member");
        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                member.clear();
                for( DataSnapshot child:dataSnapshot.getChildren()){
                    member.add((String)child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AddMember.this,"Failed ."+databaseError.toString(),Toast.LENGTH_LONG).show();
                Log.e("ERROR",databaseError.toString());
            }
        });

    }

    public void Add(View view) {
        name = txtName.getText().toString();
        RegisterDialog obj = new RegisterDialog();
        FragmentManager fm =getSupportFragmentManager();
        Bundle arg = new Bundle();
        obj.show(fm,"SELECT");
    }

    @Override
    public void onFinishEditDialog(String email, String pass) {
        this.email= email;
        this.pass = pass;
    }

    @Override
    public void Dismiss() {
        final FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fAuth.createUserWithEmailAndPassword(email,pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = fAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();
                        user.updateProfile(profileUpdates);
                        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Member");
                        db1.child(name).setValue(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddMember.this,"Added..",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddMember.this,"Failed ."+e.toString(),Toast.LENGTH_LONG).show();
                                Log.e("ERROR",e.toString());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}

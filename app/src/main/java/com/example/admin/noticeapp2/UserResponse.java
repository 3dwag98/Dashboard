package com.example.admin.noticeapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserResponse extends AppCompatActivity {

    private EditText txtResponse;
    private Button btnSubmit;
    private ImageButton imgBack;
    private AppCompatSpinner Tospin;
    ArrayList<String> list;
    Toolbar toolbar;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.action_forum:
                finish();
                startActivity(new Intent(UserResponse.this,UserForum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_notices:
                finish();
                startActivity(new Intent(UserResponse.this,UserNotice.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_feedback:
                finish();
                startActivity(new Intent(UserResponse.this,Help_page.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(UserResponse.this,Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_response);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Response ");

        txtResponse = findViewById(R.id.txtResponse);
        btnSubmit = findViewById(R.id.btnSubmit);
        Tospin = findViewById(R.id.toList);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Response ");
        list = new ArrayList<>();
        final ArrayAdapter<ArrayList> adapter = new ArrayAdapter(this,
                android.R.layout.test_list_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Tospin.setAdapter(adapter);
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Member");
        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for( DataSnapshot child:dataSnapshot.getChildren()){
                    list.add(child.getKey().toString());
                }
                list.add("Admin");
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserResponse.this,"Failed ."+databaseError.toString(),Toast.LENGTH_LONG).show();
                Log.e("ERROR",databaseError.toString());
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
        String date = String.valueOf((new Date().getTime()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String from =user.getDisplayName();
        String  querystr = txtResponse.getText().toString();
        Query query1 =  new Query(Tospin.getSelectedItem().toString(),from,querystr, Calendar.getInstance().getTime());
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Query");
        db1.child(date).setValue(query1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UserResponse.this,"Submitted",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserResponse.this,"Failed ."+e.toString(),Toast.LENGTH_LONG).show();
                Log.e("EROR",e.toString());

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),new_dashboard.class));
    }
}

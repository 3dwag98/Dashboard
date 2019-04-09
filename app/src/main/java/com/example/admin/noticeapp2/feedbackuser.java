package com.example.admin.noticeapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class feedbackuser extends AppCompatActivity {

   Toolbar toolbar;
   ListView listview;
   List<String> list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_forum:
                finish();
                startActivity(new Intent(feedbackuser.this, Forum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.action_logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(feedbackuser.this, Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbackuser);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview = findViewById(R.id.list);

        list = new ArrayList<>();
        ArrayAdapter<String> ad = new ArrayAdapter(feedbackuser.this,android.R.layout.simple_list_item_1,list);
        listview.setAdapter(ad);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        Query query = FirebaseDatabase.getInstance().getReference("Query");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    if(child.child("to").getValue().equals("member2")){
                        com.example.admin.noticeapp2.Query item = child.getValue(com.example.admin.noticeapp2.Query.class);
                        list.add(item.getDate().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}

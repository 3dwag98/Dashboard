
package com.example.admin.noticeapp2;



import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CardDemoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    DatabaseReference ref;
    List<Notice> notices ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_notices);
        notices = new ArrayList<Notice>();


        Log.v("Hello", "oncreate finished");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Hello", "onstart");

        ref = FirebaseDatabase.getInstance().getReference("Notices");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                for(DataSnapshot notice:dataSnapshot.getChildren()){
                    Notice n1 = notice.getValue(Notice.class);
                    Notice n2 = new Notice();
                    String title = n1.getDes();
                    String des = n1.getTitle();
                    n2.setTitle(title);
                    n2.setDes(des);
                    notices.add(n2);
                    Log.v("Hello","data reading");
                    Log.v("Hello",n2.getTitle());
                    Log.v("Hello",n2.getDes());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("Hello", "Failed to read value.", databaseError.toException());
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(notices,this);
        recyclerView.setAdapter(adapter);
        Log.v("Hello", "onstart finish");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
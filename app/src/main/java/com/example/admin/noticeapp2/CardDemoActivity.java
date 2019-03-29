
package com.example.admin.noticeapp2;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CardDemoActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private ProgressDialog progress;
    private DatabaseReference mDatabase;
    private List<Notice> uploads;
    private MyAdpater ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_notices);
        recyclerView = findViewById(R.id.recycler_view);

        linearLayoutManager = new LinearLayoutManager(CardDemoActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        uploads = new ArrayList<>();



        Query q = FirebaseDatabase.getInstance().getReference("Notices").orderByChild("time/time");

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Notice upload = postSnapshot.getValue(Notice.class);
                    uploads.add(upload);
                }
                Collections.reverse(uploads);
                ad = new MyAdpater(getApplicationContext(), uploads);
                recyclerView.setAdapter(ad);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(CardDemoActivity.this,new_dashboard.class));
    }

}
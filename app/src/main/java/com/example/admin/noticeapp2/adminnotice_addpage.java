
package com.example.admin.noticeapp2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adminnotice_addpage extends AppCompatActivity {

    private ImageButton imgBack,imgAdd;
    private RecyclerView rcView;
    private TextView txtError;
    private LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    DatabaseReference ref;
    List<Notice> notices ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notices);

        imgBack = findViewById(R.id.imgBack);
        imgAdd = findViewById(R.id.imgAdd);
        rcView = findViewById(R.id.recycler_view);
        txtError = findViewById(R.id.txtError);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),admin_dashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),add_notice.class));
            }
        });

        notices = new ArrayList<Notice>();
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

    }



    @Override
    protected void onStart() {
        super.onStart();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(notices,this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),admin_dashboard.class));
    }
}

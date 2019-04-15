
package com.example.admin.noticeapp2;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UserNotice extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private ProgressDialog progress;
    private DatabaseReference mDatabase;
    private List<Notice> uploads,uploadall;
    private MyAdpater ad;
    Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_notice_menu,menu);
        MenuItem searchitem = menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) searchitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    uploads.clear();
                    uploads.addAll(uploadall);
                    ad.notifyDataSetChanged();
                }
                ad.getFilter().filter(s);
                return false;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                uploads.clear();
                uploads.addAll(uploadall);
                ad.notifyDataSetChanged();
                return false;
            }
        });

        searchitem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                uploads.clear();
                uploads.addAll(uploadall);
                ad.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.action_forum:
                finish();
                startActivity(new Intent(UserNotice.this,UserForum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_notices:

                break;

            case R.id.action_response:
                finish();
                startActivity(new Intent(UserNotice.this,UserResponse.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.action_feedback:
                finish();
                startActivity(new Intent(UserNotice.this,Help_page.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(UserNotice.this,Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notices);
        recyclerView = findViewById(R.id.recycler_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notices ");

        linearLayoutManager = new LinearLayoutManager(UserNotice.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        uploads = new ArrayList<>();
        uploadall = new ArrayList<>();

        Query q = FirebaseDatabase.getInstance().getReference("Notices").orderByChild("time/time");

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Notice upload = postSnapshot.getValue(Notice.class);
                    uploads.add(upload);
                }
                Collections.reverse(uploads);
                uploadall.addAll(uploads);
                ad = new MyAdpater(UserNotice.this, uploads);
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
        startActivity(new Intent(UserNotice.this,new_dashboard.class));
    }
}
package com.example.admin.noticeapp2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Help_page extends AppCompatActivity {
    ListView listview;
    List<com.example.admin.noticeapp2.Query> list;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;

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
                startActivity(new Intent(Help_page.this,UserForum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_notices:
                finish();
                startActivity(new Intent(Help_page.this,UserNotice.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_feedback:
               break;

            case R.id.action_response:
                finish();
                startActivity(new Intent(Help_page.this,UserResponse.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.action_logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Help_page.this,Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_help_page);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedback ");

        listview = findViewById(R.id.listview);
        list = new ArrayList<>();

        final QueryAdapter ad =  new QueryAdapter(getApplicationContext(),list);
        listview.setAdapter(ad);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        com.google.firebase.database.Query query = FirebaseDatabase.getInstance().getReference("Feedback");
     try{
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    com.example.admin.noticeapp2.Query item = child.getValue(com.example.admin.noticeapp2.Query.class);
                       if(item.getTo().equals(user.getDisplayName())) {
                           list.add(item);
                       }
                }
                ad.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
     }catch(Exception ex){
         Log.e("feedback",ex.toString());
     }

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(Help_page.this,new_dashboard.class));
    }

    public class QueryAdapter extends ArrayAdapter<com.example.admin.noticeapp2.Query> {

        public QueryAdapter(Context applicationContext, List<com.example.admin.noticeapp2.Query> list) {
            super(applicationContext,0,list);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Query item = getItem(position);
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView tvName = convertView.findViewById(android.R.id.text1);
            tvName.setText(item.getFrom());
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString("feedback",item.getFeedback());
                    args.putString("query",item.getQuery());
                    args.putString("from",item.getFrom());
                    args.putString("to",item.getTo());
                    args.putString("time",item.getDate().toString());
                    Answer object = new Answer();
                    FragmentManager fm = getSupportFragmentManager();
                    object.setArguments(args);
                    object.show(fm,"ans");
                }
            });
            return convertView;
        }
    }
}

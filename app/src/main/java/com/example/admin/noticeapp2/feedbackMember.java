package com.example.admin.noticeapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class feedbackMember extends AppCompatActivity {
   Toolbar toolbar;
   ListView listview;
   List<com.example.admin.noticeapp2.Query> list;
    private SharedPreferences memberlogin;
    private SharedPreferences.Editor memEditor;
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
                startActivity(new Intent(feedbackMember.this, Forum.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.action_logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                memberlogin = getSharedPreferences("memberPref", MODE_PRIVATE);
                memEditor = memberlogin.edit();
                memEditor.putBoolean("saveLogin",false);
                memEditor.commit();
                finish();
                startActivity(new Intent(feedbackMember.this, Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_member);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedback");

        listview = findViewById(R.id.list);
        list = new ArrayList<>();

        final QueryAdapter ad =  new QueryAdapter(getApplicationContext(),list);
        listview.setAdapter(ad);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        Query query = FirebaseDatabase.getInstance().getReference("Query");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot child:dataSnapshot.getChildren()){
                    com.example.admin.noticeapp2.Query item = child.getValue(com.example.admin.noticeapp2.Query.class);
                    if(item.getTo().equals(name)){
                        list.add(item);
                        Log.e("feedback",list.size()+"Size");
                    }
                }
                ad.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    class QueryAdapter extends ArrayAdapter<com.example.admin.noticeapp2.Query>{

        public QueryAdapter( Context context,  @NonNull List<com.example.admin.noticeapp2.Query> objects) {
            super(context, 0, objects);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final com.example.admin.noticeapp2.Query item = getItem(position);
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView tvName = convertView.findViewById(android.R.id.text1);
            tvName.setText(item.getFrom());
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString("from",item.getFrom());
                    args.putString("query",item.getQuery());
                    args.putString("time",item.getDate().toString());
                    FeedbackDialog object = new FeedbackDialog();
                    FragmentManager fm = getSupportFragmentManager();
                    object.setArguments(args);
                    object.show(fm,"Feedback");
                }
            });
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(feedbackMember.this,Forum.class));
    }
}

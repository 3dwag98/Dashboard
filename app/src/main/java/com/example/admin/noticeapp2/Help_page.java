package com.example.admin.noticeapp2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
    private ImageButton imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_help_page);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Help_page.this,new_dashboard.class));
            }
        });


        listview = findViewById(R.id.listview);
        list = new ArrayList<>();

        final Help_page.QueryAdapter ad =  new Help_page.QueryAdapter(getApplicationContext(),list);
        listview.setAdapter(ad);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        com.google.firebase.database.Query query = FirebaseDatabase.getInstance().getReference("Feedback");
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
                    args.putString("feedback",item.getFeedback());
                    args.putString("query",item.getQuery());
                    args.putString("from",item.getFrom());
                    args.putString("to",item.getTo());
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

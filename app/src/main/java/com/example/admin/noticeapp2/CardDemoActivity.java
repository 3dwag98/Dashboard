
package com.example.admin.noticeapp2;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class CardDemoActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_notices);
        recyclerView = findViewById(R.id.recycler_view);

        pg = new ProgressDialog(this);
        pg.setMessage("Fetching.....");
        pg.setTitle("Progess Dialog");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.show();
        linearLayoutManager = new LinearLayoutManager(CardDemoActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();
        pg.dismiss();


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout root;
        public TextView txtTitle;
        public TextView txtDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.item_noticetitle);
            txtDesc = itemView.findViewById(R.id.item_detail);
        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }


        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }
    }


    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Notices");

        FirebaseRecyclerOptions<Notice> options =
                new FirebaseRecyclerOptions.Builder<Notice>()
                        .setQuery(query, Notice.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Notice, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull Notice model) {
                holder.setTxtTitle(model.getTitle());
                holder.setTxtDesc(model.getDes());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CardDemoActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_layout, parent, false);

                return new ViewHolder(view);
            }



        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(CardDemoActivity.this,new_dashboard.class));

    }
}
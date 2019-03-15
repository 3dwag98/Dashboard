
package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class adminnotice_addpage extends AppCompatActivity {

    private ImageButton imgBack,imgAdd;

    private TextView txtError;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DbAdapter adapter;
    ProgressDialog pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notices);

        imgBack = findViewById(R.id.imgBack);
        imgAdd = findViewById(R.id.imgAdd);
        recyclerView = findViewById(R.id.recycler_view);
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

        pg = new ProgressDialog(this);
        pg.setMessage("Fetching.....");
        pg.setTitle("Progess Dialog");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        linearLayoutManager = new LinearLayoutManager(adminnotice_addpage.this);
        recyclerView.setLayoutManager(linearLayoutManager);
       // recyclerView.setHasFixedSize(true);
        fetch();


    }

//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public RelativeLayout root;
//        public TextView txtTitle;
//        public TextView txtDesc;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            root = itemView.findViewById(R.id.list_root);
//            txtTitle = itemView.findViewById(R.id.item_noticetitle);
//            txtDesc = itemView.findViewById(R.id.item_detail);
//        }
//
//        public void setTxtTitle(String string) {
//            txtTitle.setText(string);
//        }
//
//
//        public void setTxtDesc(String string) {
//            txtDesc.setText(string);
//        }
//    }

    private void fetch() {
        pg.show();
        txtError.setVisibility(EditText.INVISIBLE);
        final Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Notices");

        FirebaseRecyclerOptions<Notice> options =
                new FirebaseRecyclerOptions.Builder<Notice>()
                        .setQuery(query, Notice.class)
                        .build();


        adapter = new DbAdapter(options,this);

//        adapter = new FirebaseRecyclerAdapter<Notice, adminnotice_addpage.ViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull adminnotice_addpage.ViewHolder holder, final int position, @NonNull Notice model) {
//                holder.setTxtTitle(model.getTitle());
//                holder.setTxtDesc(model.getDes());
//
//                holder.root.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(adminnotice_addpage.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public adminnotice_addpage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.card_layout, parent, false);
//
//                return new adminnotice_addpage.ViewHolder(view);
//            }
//            public void deleteItem(int position){
//                getSnapshots().getSnapshot(position).getRef().removeValue();
//            }
//
//
//        };


        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.v("hello",viewHolder.getAdapterPosition()+":position");

                Log.v("hello",adapter.getItemCount()+":total");
               // adapter.DeleteItem(viewHolder.getAdapterPosition());
//                adapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).getRef().removeValue();
//                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//                adapter.notifyDataSetChanged();
                adapter.DeleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        pg.dismiss();
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
        startActivity(new Intent(getApplicationContext(),admin_dashboard.class));
    }
}

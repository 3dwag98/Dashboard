
package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class adminnotice_addpage extends AppCompatActivity {

    private ImageButton imgBack,imgAdd;

    private TextView txtError;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MyAdpater ad;
    private DatabaseReference mDatabase;
    private List<Notice> uploads;


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
        pg.setTitle("Progess com.example.admin.noticeapp2.Dialog");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        linearLayoutManager = new LinearLayoutManager(adminnotice_addpage.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        uploads = new ArrayList<>();


        Query q = FirebaseDatabase.getInstance().getReference("Notices").orderByChild("time/time");

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Notice upload = postSnapshot.getValue(Notice.class);
                    uploads.add(upload);
                }
                //creating adapter
                ad = new MyAdpater(getApplicationContext(), uploads);

                //adding adapter to recyclerview
                recyclerView.setAdapter(ad);
                Log.v("LENGTH",uploads.size()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        txtError.setText(null);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                final int pos = viewHolder.getAdapterPosition();
//                try{
//
//                    String url = uploads.get(pos).getUpload();
//                    final String name=uploads.get(viewHolder.getAdapterPosition()).getTitle();
//                    int p=0;
//                    for(Notice model : uploads) {
//                        Log.e("DATAL","pos:" + (p++)+",name:"+model.getTitle());
//                    }
//
//
////                if(!(url.equals(""))) {
////                    try {
////                        StorageReference mref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
////                        mref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
////                            @Override
////                            public void onSuccess(Void aVoid) {
////                                Toast.makeText(adminnotice_addpage.this, "deleted ...", Toast.LENGTH_SHORT).show();
////
////                            }
////                        }).addOnFailureListener(new OnFailureListener() {
////                            @Override
////                            public void onFailure(@NonNull Exception e) {
////                                Log.v("dataFail:  ", "" + e);
////                                Toast.makeText(adminnotice_addpage.this, "" + e, Toast.LENGTH_SHORT).show();
////                            }
////                        });
////                    } catch (Exception ex) {
////                        Log.v("dataError:  ", "" + ex);
////                        Toast.makeText(adminnotice_addpage.this, "" + ex, Toast.LENGTH_SHORT).show();
////                    }
////                }
//                    DatabaseReference rRef = FirebaseDatabase.getInstance().getReference("Notices").child(name);
//
//                    rRef.getRef().removeValue(new DatabaseReference.CompletionListener() {
//                        @Override
//                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//
//                            Log.e("DATAL","pos:" + pos+",name:"+name+"data:");
//
//                        }
//                    });
//
//                    p=0;
//                    for(Notice model : uploads) {
//                        Log.e("DATAL","afterpos:" + (p++)+",name:"+model.getTitle());
//                    }
////                    uploads.remove(viewHolder.getAdapterPosition());
////                    ad.notifyItemRemoved(viewHolder.getAdapterPosition());
//                }catch (Exception e){
//                    Toast.makeText(adminnotice_addpage.this,e+"",Toast.LENGTH_LONG);
//                }
                uploads.remove(pos);
                ad.notifyItemRemoved(pos);
               // ad.notifyDataSetChanged();
            }

        }).attachToRecyclerView(recyclerView);

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
//                final int pos = viewHolder.getAdapterPosition();
//                String url = uploads.get(viewHolder.getAdapterPosition()).getUpload();
//
//                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.hasChild(uploads.get(viewHolder.getAdapterPosition()).getTitle())){
//                        dataSnapshot.child(uploads.get(viewHolder.getAdapterPosition()).getTitle()).getRef().removeValue();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//                if(!url.equals("")) {
//                    try {
//                        StorageReference mref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
//                        mref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(adminnotice_addpage.this, "deleted ...", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.v("dataFail:  ", "" + e);
//                                Toast.makeText(adminnotice_addpage.this, "" + e, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } catch (Exception ex) {
//                        Log.v("dataError:  ", "" + ex);
//                        Toast.makeText(adminnotice_addpage.this, "" + ex, Toast.LENGTH_SHORT).show();
//                    }
//                }
//                uploads.remove(pos);
//                ad.notifyItemRemoved(pos);
//            }
//        }).attachToRecyclerView(recyclerView);



    }


    @Override
    protected void onStart() {
        super.onStart();


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

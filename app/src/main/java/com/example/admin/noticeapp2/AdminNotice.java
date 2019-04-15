
package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminNotice extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MyAdpater ad;
    private DatabaseReference mDatabase;
    private List<Notice> uploads,uploadall,uploaddummy;
    Toolbar toolbar;
    ProgressDialog pg;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_dashboard_notice_menu,menu);
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

            case R.id.action_add:
                finish();
                startActivity(new Intent(AdminNotice.this,add_notice.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_notices:

                break;

            case R.id.action_response:
                finish();
                startActivity(new Intent(AdminNotice.this,feedbackAdmin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.action_member:
                finish();
                startActivity(new Intent(AdminNotice.this,AddMember.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;

            case R.id.action_logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(AdminNotice.this,Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notices);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notices ");

        recyclerView = findViewById(R.id.recycler_view);


        pg = new ProgressDialog(this);
        pg.setMessage("Fetching.....");
        pg.setTitle("Progess Dialog");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        linearLayoutManager = new LinearLayoutManager(AdminNotice.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        uploads = new ArrayList<>();
        uploadall = new ArrayList<>();
        uploaddummy = new ArrayList<>();
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
                Collections.reverse(uploads);
                uploadall.addAll(uploads);
                uploaddummy.addAll(uploads);
                ad = new MyAdpater(AdminNotice.this, uploads);
                //adding adapter to recyclerview
                recyclerView.setAdapter(ad);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                final int pos = viewHolder.getAdapterPosition();
                try{

                    String url = uploads.get(pos).getUpload();
                    final String name=uploads.get(pos).getTitle();
                    int p=0;
                    for(Notice model : uploads) {
                        Log.e("DATAL","pos:" + (p++)+",name:"+model.getTitle());
                    }
                if(!(url.equals(""))) {
                    try {
                        StorageReference mref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                        mref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminNotice.this, "Data deleted" , Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v("dataFail:  ", "" + e);
                                Toast.makeText(AdminNotice.this, "" + e, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception ex) {
                        Log.v("dataError:  ", "" + ex);
                        Toast.makeText(AdminNotice.this, "" + ex, Toast.LENGTH_SHORT).show();
                    }
                }
                    DatabaseReference rRef = FirebaseDatabase.getInstance().getReference("Notices").child(name);

                    rRef.getRef().removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete( DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            Toast.makeText(AdminNotice.this, "Notice deleted" , Toast.LENGTH_SHORT).show();
                            Log.e("DATAL","pos:" + pos+",name:"+name+"data:");

                        }
                    });

                    p=0;
                    for(Notice model : uploads) {
                        Log.e("DATAL","afterpos:" + (p++)+",name:"+model.getTitle());
                    }
////                    uploads.remove(viewHolder.getAdapterPosition());
////                    ad.notifyItemRemoved(viewHolder.getAdapterPosition());
                }catch (Exception e){
                    Toast.makeText(AdminNotice.this,e+"",Toast.LENGTH_LONG);
                }
                uploads.remove(pos);
//                uploads.clear();
//                uploaddummy.remove(pos);
//                uploads.addAll(uploaddummy);
               ad.notifyItemRemoved(pos);
               ad.notifyItemRangeChanged(0,uploads.size());
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
//                                Toast.makeText(AdminNotice.this, "deleted ...", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.v("dataFail:  ", "" + e);
//                                Toast.makeText(AdminNotice.this, "" + e, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    } catch (Exception ex) {
//                        Log.v("dataError:  ", "" + ex);
//                        Toast.makeText(AdminNotice.this, "" + ex, Toast.LENGTH_SHORT).show();
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

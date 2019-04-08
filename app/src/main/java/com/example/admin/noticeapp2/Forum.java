package com.example.admin.noticeapp2;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Forum extends AppCompatActivity implements Dialog.DialogListener, Dialog.Listener {
    private EditText txtmsg;
    private ImageView imgAttach,imgSend;
    private RecyclerView rc;
    ForumAdapter ad;
    List<Model> list,listcopy,listall;
    Toolbar toolbar;
    String content ,tag;
    String msg;
    String[] liststr = {"ALL","FY","SY","TY"};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem spinner = menu.findItem(R.id.spinner);
        Spinner sp = (Spinner) spinner.getActionView();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(Forum.this,
                android.R.layout.simple_spinner_item, liststr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ((!listall.isEmpty())) {
                    if (liststr[i].equals("ALL")) {
                        listcopy.clear();
                        listcopy.addAll(listall);
                    } else {
                        listcopy.clear();
                        for (Model item : listall) {
                            if (item.getTag().equalsIgnoreCase(liststr[i])) {
                                listcopy.add(item);
                            }
                        }
                    }
                    list.clear();
                    list.addAll(listcopy);
                    ad.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        MenuItem searchitem =  menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) searchitem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                ad.getFilter().filter(s);
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
                list.clear();
                list.addAll(listall);
                ad.notifyDataSetChanged();
                return true;
            }
        });


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtmsg  = findViewById(R.id.txtmsg);
        imgSend = findViewById(R.id.imgsend);
        rc = findViewById(R.id.rcview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Forum.this);
        rc.setLayoutManager(linearLayoutManager);
        rc.setHasFixedSize(true);
        list = new ArrayList<Model>();
        listall = new ArrayList<Model>();
        listcopy = new ArrayList<Model>();
        Query q = FirebaseDatabase.getInstance().getReference("Notes").orderByChild("date/time");

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Model item = postSnapshot.getValue(Model.class);
                    list.add(item);
                }
                Collections.reverse(list);
                listall.addAll(list);
                ad = new ForumAdapter(Forum.this, list);
                ad.notifyDataSetChanged();
                rc.setAdapter(ad);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void Send(View view) {
        this.msg = txtmsg.getText().toString();
        Dialog obj = new Dialog();
        FragmentManager fm =getSupportFragmentManager();
        Bundle arg = new Bundle();
        arg.putString("child",msg);
        obj.setArguments(arg);
        obj.show(fm,"SELECT");
        final String[] from = new String[1];


        //getFrom
//        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Faculty");
//        db.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(mAuth.getCurrentUser().getDisplayName())){
//                    from[0] = mAuth.getCurrentUser().getDisplayName().toString();
//                }
//                else{
//                    from[0] = "NULL";
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }

    private void send() {
        Model item = new Model("NULL",this.tag,msg,this.content,Calendar.getInstance().getTime());
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Notes");
        db1.child(msg).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Forum.this,"Added..",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Forum.this,"Failed ."+e.toString(),Toast.LENGTH_LONG).show();
                Log.e("EROR",e.toString());
            }
        });
    }

    @Override
    public void onFinishEditDialog(String inputText, String tag) {
        this.content = inputText;
        this.tag = tag;
    }

    @Override
    public void onDismiss() {
        send();
    }
}

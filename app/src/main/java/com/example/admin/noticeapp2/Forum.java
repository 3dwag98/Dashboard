package com.example.admin.noticeapp2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;
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
    private ImageView imgSend;
    private RecyclerView rc;
    ForumAdapter ad;
    List<Model> list,listcopy,listall;
    Toolbar toolbar;
    String content ,tag;
    String msg,from;
    String[] liststr = {"ALL","FY","SY","TY"};
    private SharedPreferences loginPreferences,memberlogin;
    private SharedPreferences.Editor memEditor;

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

        switch (item.getItemId()){
            case R.id.action_query:
                finish();
                startActivity(new Intent(Forum.this, feedbackMember.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.action_logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                memberlogin = getSharedPreferences("memberPref", MODE_PRIVATE);
                memEditor = memberlogin.edit();
                memEditor.putBoolean("saveLogin",false);
                memEditor.commit();
                finish();
                startActivity(new Intent(Forum.this,Login_Window.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forum");

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
    }

    private void send() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        this.from = user.getDisplayName();
        Model item = new Model(this.from,this.tag,msg,this.content,Calendar.getInstance().getTime());
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
                Log.e("ERROR",e.toString());
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Close Application");
        builder.setMessage("Do you want Exit ? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }
}

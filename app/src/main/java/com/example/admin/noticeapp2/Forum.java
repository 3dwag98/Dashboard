package com.example.admin.noticeapp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Forum extends AppCompatActivity {
    private EditText txtmsg;
    private ImageView imgAttach,imgSend;
    private RecyclerView rc;
    ForumAdapter ad;
    List<Model> list;
    Toolbar toolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
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
        imgAttach =  findViewById(R.id.imgattach);
        imgSend = findViewById(R.id.imgsend);
        rc = findViewById(R.id.rcview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Forum.this);
        rc.setLayoutManager(linearLayoutManager);
        rc.setHasFixedSize(true);
        list = new ArrayList<Model>();
        list.add(new Model("chintu1","all", Calendar.getInstance().getTime(),"Descrpytion"));
        list.add(new Model("chintu2","all", Calendar.getInstance().getTime(),"9987383647"));
        list.add(new Model("chintu3","all", Calendar.getInstance().getTime(),"gawdechint98@gmail.com"));
        list.add(new Model("chintu4","all", Calendar.getInstance().getTime(),"www.google.com"));
        ad  = new ForumAdapter(getApplicationContext(),list);
        rc.setAdapter(ad);
    }

    public void Attach(View view) {

    }

    public void Send(View view) {

    }
}

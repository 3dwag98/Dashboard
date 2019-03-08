
package com.example.admin.noticeapp2;



import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;

import android.view.Menu;

import android.view.MenuItem;


public class CardDemoActivity extends AppCompatActivity {



    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;

    RecyclerView.Adapter adapter;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.cardview_notices);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // setSupportActionBar(toolbar);



        recyclerView =

                (RecyclerView) findViewById(R.id.recycler_view);



        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);



        adapter = new RecyclerAdapter();

        recyclerView.setAdapter(adapter);

    }



}
package com.example.admin.noticeapp2;

import android.support.design.widget.Snackbar;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {



    private String[] titles = {" One",

            " Two",

            " Three",
    };



    private String[] details = {"event is on 5th april",

            "result will be display on the notice board", "tommorow there ill be holiday",

            };






    class ViewHolder extends RecyclerView.ViewHolder{



        public int currentItem;

        public ImageView itemImage;

        public TextView itemTitle;

        public TextView itemDetail;



        public ViewHolder(View itemView) {

            super(itemView);


            itemTitle = (TextView)itemView.findViewById(R.id.item_noticetitle);

            itemDetail =

                    (TextView)itemView.findViewById(R.id.item_detail);



            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {

                    int position = getAdapterPosition();



                    Snackbar.make(v, "Click detected on item " + position,

                            Snackbar.LENGTH_LONG)

                            .setAction("Action", null).show();



                }

            });

        }

    }



    @Override

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())

                .inflate(R.layout.card_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }



    @Override

    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.itemTitle.setText(titles[i]);

        viewHolder.itemDetail.setText(details[i]);



    }



    @Override

    public int getItemCount() {

        return titles.length;

    }

}
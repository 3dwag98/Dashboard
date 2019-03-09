package com.example.admin.noticeapp2;

import android.content.Context;
import android.support.design.widget.Snackbar;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<Notice> notice ;
    Context context;
    private String[] titles = {" One",
            " Two",
            " Three",
    };
    private String[] details = {"event is on 5th april",
            "result will be display on the notice board", "tommorow there ill be holiday",
            };

    public RecyclerAdapter(List<Notice> notice, Context c){
        this.notice = notice;
        this.context =  c;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;



        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView)itemView.findViewById(R.id.item_noticetitle);
            itemDetail =(TextView)itemView.findViewById(R.id.item_detail);
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
        View v = LayoutInflater.from(this.context)
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Notice list = notice.get(i);
        viewHolder.itemTitle.setText(list.getTitle());
        viewHolder.itemDetail.setText(list.getDes());

    }
    @Override
    public int getItemCount() {
        int arr = 0;
        try{
            if(notice.size()==0){
                arr = 0;
            }
            else{
                arr=notice.size();
            }

        }catch (Exception e){
        }
        return arr;
    }
}
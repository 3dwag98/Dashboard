package com.example.admin.noticeapp2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> implements Filterable {
    Context context;
    List<Model> list;

    public ForumAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ForumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.forum_card, viewGroup, false);
        ForumAdapter.ViewHolder viewHolder = new ForumAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumAdapter.ViewHolder viewHolder, int i) {
        Model item =  list.get(i);
        viewHolder.from.setText(item.getFrom());
        viewHolder.tag.setText(item.getTag());
        final Date time = item.getDate();
        final String time1 = new SimpleDateFormat("dd MMMM").format(time);
        viewHolder.date.setText(time1);
        if(false){
            //change Image
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView root ;
        public TextView from,tag,date;
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.forum_card);
            from = itemView.findViewById(R.id.from);
            tag = itemView.findViewById(R.id.tag);
            img = itemView.findViewById(R.id.img);
            date  = itemView.findViewById(R.id.date);
        }
    }
}

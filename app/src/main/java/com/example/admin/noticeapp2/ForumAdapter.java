package com.example.admin.noticeapp2;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ViewHolder> implements Filterable {
    Context context;
    List<Model> list;
    List<Model> listfull;

    public ForumAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
        this.listfull = list;
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
    public void onBindViewHolder(@NonNull final ForumAdapter.ViewHolder viewHolder, int i) {
        final Model item =  list.get(i);
        viewHolder.from.setText(capitalizer(item.getFrom()));

        viewHolder.from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QueryDialog obj = new QueryDialog();
                Bundle arg =  new Bundle();
                arg.putString("TO",viewHolder.from.getText().toString());
                obj.setArguments(arg);
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                obj.show(fm,"Query");
            }
        });
        viewHolder.tag.setText(item.getTag());
        final Date time = item.getDate();
        final String time1 = new SimpleDateFormat("dd MMMM").format(time);
        viewHolder.date.setText(time1);
        viewHolder.descp.setText(item.getDescrp());
        viewHolder.title.setText(capitalizer(item.getMsg()));

        if(item.getUpload() != null) {

            List<String> imageFormat = Arrays.asList(new String[]{"jpg", "JPG", "png", "PNG", "jpeg", "JPEG"});
            String type = item.getType();
            if (imageFormat.contains(type) && (!type.equals(""))) {
                final float scale = context.getResources().getDisplayMetrics().density;
                viewHolder.img.getLayoutParams().height = (int) (200*scale);
                viewHolder.img.getLayoutParams().width = (int) (350*scale);
                viewHolder.img.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context)
                        .load(item.getUpload())
                        .into(viewHolder.img);
            } else if ((!type.isEmpty()) || type.contains("")) {
                final float scale = context.getResources().getDisplayMetrics().density;
                viewHolder.img.getLayoutParams().height = (int) (200*scale);
                viewHolder.img.getLayoutParams().width = (int) (350*scale);
                viewHolder.img.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context)
                        .load(R.drawable.book)
                        .into(viewHolder.img);
            } else {

            }
           viewHolder.root.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "wallpaper");

                   if (!mediaStorageDir.exists()) {
                       if (!mediaStorageDir.mkdirs()) {
                           Log.d("App", "failed to create directory");
                       }
                   }
                   String dir = mediaStorageDir.getAbsolutePath();
                   new FileOP(context).download(context,item.getMsg(),"",dir,item.getUpload());
                   Toast.makeText(context,"Downloading....",Toast.LENGTH_LONG).show();
               }
           });
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Model> filteredlist = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0 || charSequence.equals("")) {
                filteredlist.addAll(listfull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Model item : listfull) {
                    if (item.getDescrp().toLowerCase().contains(filterPattern)) {
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends Model>) filterResults.values);
            notifyDataSetChanged();
        }
    };



    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView root ;
        public TextView from,tag,date,descp,title;
        public ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.forum_card);
            from = itemView.findViewById(R.id.from);
            tag = itemView.findViewById(R.id.tag);
            img = itemView.findViewById(R.id.img);
            date  = itemView.findViewById(R.id.date);
            descp = itemView.findViewById(R.id.content);
            title = itemView.findViewById(R.id.Titleforum);
        }
    }
    private String capitalizer(String word){

        String[] words = word.split(" ");
        StringBuilder sb = new StringBuilder();
        if (words[0].length() > 0) {
            sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
            for (int i = 1; i < words.length; i++) {
                sb.append(" ");
                sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
            }
        }
        return  sb.toString();

    }
}

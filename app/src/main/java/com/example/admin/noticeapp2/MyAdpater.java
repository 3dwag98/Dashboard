package com.example.admin.noticeapp2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyAdpater extends RecyclerView.Adapter<MyAdpater.ViewHolder> implements Filterable {
    private Context context;
    private List<Notice> uploads;
    private List<Notice> uploadsfull;


    public MyAdpater(Context context, List<Notice> uploads) {
        this.uploads = uploads;
        this.context = context;
        this.uploadsfull = uploads;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Notice item = (Notice) uploads.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtDesc.setText(item.getDescrp());
        final Date time = item.getTime();
        final String time1 = new SimpleDateFormat("dd MMMM yyyy").format(time);
        holder.txtTime.setText(time1);

        List<String> imageFormat = Arrays.asList(new String[]{"jpg", "JPG", "png", "PNG", "jpeg", "JPEG"});
        if(imageFormat.contains(item.getType()) && (!item.getType().equals(""))){
            Glide.with(context)
                    .load(item.getUpload())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imgPreview);
        }else{
            Glide.with(context)
                    .load(R.drawable.ic_cloud_done)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imgPreview);;
        }

        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
                builder.setTitle("Delete Notice");
                builder.setMessage("Do you want to Delete Notices");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeItem(position);
                        Toast.makeText(context,"Deleted Notice",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
                return false;
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = item.getTitle();
                String descrp = item.getDescrp();
                String time = time1;
                String type = item.getType();
                Toast.makeText(context,title,Toast.LENGTH_SHORT).show();

                String url = item.getUpload();

                DialogViewNotice obj = new DialogViewNotice();
                Bundle arg =  new Bundle();
                arg.putString("title",title);
                arg.putString("file",url);
                arg.putString("type",type);
                arg.putString("des",descrp);
                arg.putString("date",time);
                obj.setArguments(arg);
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                obj.show(fm,"NoticeView");
            }

        });
    }

    @Override
    public int getItemCount() {
        return this.uploads.size();
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Notice> filteredlist = new ArrayList<>();
            if (charSequence.toString().isEmpty() || charSequence == null) {
                filteredlist.addAll(uploadsfull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Notice item : uploadsfull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
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
            uploads.clear();
            uploads.addAll((List) filterResults.values);
            notifyDataSetChanged();

        }
    };

    public void removeItem(final int pos){

        String url = uploads.get(pos).getUpload();
        final String name=uploads.get(pos).getTitle();
        if(!(url.equals(""))) {
            try {
                StorageReference mref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                mref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Data deleted" , Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("dataFail:  ", "" + e);
                        Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ex) {
                Log.v("dataError:  ", "" + ex);
                Toast.makeText(context, "" + ex, Toast.LENGTH_SHORT).show();
            }
        }
        DatabaseReference rRef = FirebaseDatabase.getInstance().getReference("Notices").child(name);
        rRef.getRef().removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(context, "Notice deleted" , Toast.LENGTH_SHORT).show();
                uploads.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, uploads.size());
                notifyDataSetChanged();
            }
        });


    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout root;
        public TextView txtTitle;
        public TextView txtDesc,txtTime;
        public ImageView imgPreview;
        public String text;

        public ViewHolder(View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.item_noticetitle);
            txtDesc = itemView.findViewById(R.id.item_detail);
            imgPreview = itemView.findViewById(R.id.item_image);
            txtTime = itemView.findViewById(R.id.item_time);

        }
    }
}

package com.example.admin.noticeapp2;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyAdpater extends RecyclerView.Adapter<MyAdpater.ViewHolder> implements Filterable {
    private Context context;
    private List uploads;
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
        final String time1 = new SimpleDateFormat("dd MMMM").format(time);
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


        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = item.getTitle();
                String descrp = item.getDescrp();
                String time = time1;
                String type = item.getType();
                Toast.makeText(context,title,Toast.LENGTH_SHORT).show();

                String url = item.getUpload();

                if(!url.equals("")) {
                    Intent i = new Intent(context.getApplicationContext(),NoticeView.class);
                    i.putExtra("title",title);
                    i.putExtra("descrp",descrp);
                    i.putExtra("time",time);
                    i.putExtra("url",url);
                    i.putExtra("type",type);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("NoticeUploads").child(title);
                    try {
                        final File localFile = File.createTempFile(title, type);
                        mStorageRef.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(localFile)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(browserIntent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,"Failed to load",Toast.LENGTH_LONG);
                                    }
                                });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "wallpaper");

                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d("App", "failed to create directory");
                        }
                    }
                    String dir = mediaStorageDir.getAbsolutePath();

                    download(context, item.getTitle(), "*", dir, url);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        context.startActivity(i);
                    }
                }

            }
            private void download(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {
                DownloadManager downloadmanager = (DownloadManager) context.
                        getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName);

                downloadmanager.enqueue(request);
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
            if (charSequence == null || charSequence.length() == 0|| charSequence.equals("")) {
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

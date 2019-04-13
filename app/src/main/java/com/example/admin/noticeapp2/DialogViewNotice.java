package com.example.admin.noticeapp2;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class DialogViewNotice extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_notice,container,false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle args = getArguments();

        ImageView file = view.findViewById(R.id.file);
        TextView Title =view.findViewById(R.id.title);
        TextView date=view.findViewById(R.id.date);
        TextView Descp=view.findViewById(R.id.description);
        final FileOP f= new FileOP(view.getContext());
        List<String> imageFormat = Arrays.asList(new String[]{"jpg", "JPG", "png", "PNG", "jpeg", "JPEG"});
        if(args.getString("file") != null && !args.getString("file").equals("")){
            if(imageFormat.contains(f.GetFileExtension(Uri.parse(args.getString("file"))))) {
                Glide.with(this)
                        .load(args.getString("file"))
                        .into(file);
            }else{
                Glide.with(this)
                        .load(R.drawable.ic_attachment_black_24dp)
                        .override(200,100)
                        .into(file);
            }
        }
        Title.setText(args.getString("title").toString());
        date.setText(args.getString("date").toString());
        Descp.setText(args.getString("des").toString());
        view.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(args.getString("file") != null && !args.getString("file").equals("")) {

                    File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + "wallpaper");

                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d("App", "failed to create directory");
                        }
                    }
                    String dir = mediaStorageDir.getAbsolutePath();
                    f.download(view.getContext(),args.getString("title"), "*", dir,args.getString("file"));
                    Toast.makeText(view.getContext(), "Downloading...", Toast.LENGTH_LONG).show();
                }
                dismiss();
            }
        });

    }


}

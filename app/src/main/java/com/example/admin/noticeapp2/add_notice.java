package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class add_notice extends AppCompatActivity {

    private View v ;
    private static final int IMAGE_REQUEST_CODE = 123;
    private ImageButton imgBack,imgUpload;
    private Button btnPost;
    private EditText txtTitle,txtDescrp;
    private Uri filePath ;
    private ProgressDialog progress;
    private StorageReference mStorageRef;
    private TextView txtFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_notice);
        imgBack = findViewById(R.id.imgBack);
        imgUpload = findViewById(R.id.imgUpload);
        btnPost = findViewById(R.id.btnPost);
        txtTitle = findViewById(R.id.txtTitle);
        txtDescrp = findViewById(R.id.txtDescrp);
        v = findViewById(R.id.view_upload);
        txtFileName = findViewById(R.id.filename);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        filePath = null;
        txtFileName.setText(null);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),adminnotice_addpage.class));
            }
        });

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                Post();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void Post() {

        String title = txtTitle.getText().toString();
        String Descrp = txtDescrp.getText().toString();
        String Upload = "";
        String type = GetFileExtension(filePath);
        Date time = Calendar.getInstance().getTime();
        Notice newNotice = new Notice( title, Descrp,Upload,type,time);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Notices");
        db.child(title).setValue(newNotice);
        Toast.makeText(add_notice.this,"Notice Uploaded",Toast.LENGTH_SHORT).show();

        if(filePath != null) {
            progress = new ProgressDialog(this);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("File Uploading");
            progress.setProgress(0);
            progress.show();
            final StorageReference mfile= mStorageRef.child("NoticeUploads").child(txtTitle.getText().toString());
            mfile.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progress.dismiss();
                            mfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                    db.child("Notices").child(txtTitle.getText().toString())
                                            .child("upload").setValue(url)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(add_notice.this, "File upload success", Toast.LENGTH_SHORT).show();
                                                        txtTitle.setText(null);
                                                        txtDescrp.setText(null);
                                                        imgUpload.setImageBitmap(null);
                                                        imgUpload.setBackgroundResource(R.drawable.ic_menu_gallery);
                                                        txtFileName.setText(null);
                                                    }
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.v("Upload:  ",e+"");
                                    Toast.makeText(add_notice.this, "Url :"+e, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_notice.this, "File upload unsuccessful..", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progress.setProgress(currentProgress);
                        }
                    });

        }else{
            txtTitle.setText(null);
            txtDescrp.setText(null);
        }

    }

    public String GetFileExtension(Uri uri)
    {
        if(uri == null){return "";}
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData() != null){
            filePath = data.getData();
            Bitmap btmp = null;

            String type = GetFileExtension(filePath);
            String name = getFileName(filePath);

           Snackbar.make(v, "   Selected File:   "+name,Snackbar.LENGTH_LONG).show();
            txtFileName.setText(name);
            List<String> imageFormat = Arrays.asList(new String[]{"jpg", "JPG", "png", "PNG", "jpeg", "JPEG"});
            if(imageFormat.contains(type)) {
                try {
                    btmp = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                    Bitmap compressedBitmap = Bitmap.createScaledBitmap(btmp,imgUpload.getWidth(),imgUpload.getHeight(),true);
                    imgUpload.setImageBitmap(compressedBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                imgUpload.setBackgroundResource(R.drawable.ic_cloud_done);
            }
        }
    }

    private void chooseFile() {
        Intent i = new Intent();
        i.setType("*/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Image"),IMAGE_REQUEST_CODE);
    }

    private void Upload() {
        chooseFile();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),adminnotice_addpage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}

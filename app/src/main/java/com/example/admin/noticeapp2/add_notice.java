package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class add_notice extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 123;
    private ImageButton imgBack,imgUpload;
    private Button btnPost;
    private EditText txtTitle,txtDescrp;
    private Uri filePath ;
    private ProgressDialog progress;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_notice);
        imgBack = findViewById(R.id.imgBack);
        imgUpload = findViewById(R.id.imgUpload);
        btnPost = findViewById(R.id.btnPost);
        txtTitle = findViewById(R.id.txtTitle);
        txtDescrp = findViewById(R.id.txtDescrp);
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
            @Override
            public void onClick(View view) {
                Post();
            }
        });

    }

    private void Post() {

        String title = txtTitle.getText().toString();
        String Descrp = txtDescrp.getText().toString();

        Notice newNotice = new Notice( title, Descrp);
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
                            String url;
                            url = mfile.getDownloadUrl().toString();
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                            db.child("Notices").child(txtTitle.getText().toString())
                                    .child("upload").setValue(url)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progress.dismiss();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(add_notice.this, "File upload success", Toast.LENGTH_SHORT).show();
                                            }
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData() != null){
            filePath = data.getData();
            Bitmap btmp = null;

            try {
                btmp = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                btmp.compress(Bitmap.CompressFormat.JPEG,0,stream);
//                byte[] byteArray = stream.toByteArray();
                Bitmap compressedBitmap = Bitmap.createScaledBitmap(btmp,imgUpload.getWidth(),imgUpload.getHeight(),true);
                imgUpload.setImageBitmap(compressedBitmap);

            } catch (IOException e) {
               imgUpload.setBackgroundColor(Color.YELLOW);
            }



        }
    }

    private void chooseFile() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Image"),IMAGE_REQUEST_CODE);
    }

    private void Upload() {
        imgUpload.setBackgroundColor(Color.TRANSPARENT);
       // imgUpload.setScaleType(ImageView.ScaleType.FIT_XY);
        chooseFile();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),adminnotice_addpage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}

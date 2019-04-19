package com.example.admin.noticeapp2;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class account_setup_profile extends AppCompatActivity {
    private static final int IMAGE_REQUEST_CODE = 132;
    private ImageView imgProfilepic;
    TextInputLayout txterror;
    private AppCompatSpinner txtClass;
    private EditText txtRollno;
    private TextView filename;
    String[] liststr = {"FY-BscIT","SY-BscIT","TY-BscIT"};
    private Uri filePath ;
    FileOP fileobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup_profile);

        imgProfilepic = findViewById(R.id.img_profilepic);
        txtClass = findViewById(R.id.ClassList);
        txtRollno = findViewById(R.id.txtRollno);
        filename = findViewById(R.id.filename);
        txterror = findViewById(R.id.roolInputText);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, liststr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtClass.setAdapter(adapter);

        findViewById(R.id.backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(account_setup_profile.this,Login_Window.class));
            }
        });

        imgProfilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(txtRollno.getText())){
                    Intent i = new Intent(getApplicationContext(), Registration.class);
                    if (filePath != null) {
                        i.putExtra("filePath", filePath.toString());
                    }
                    i.putExtra("class", txtClass.getSelectedItem().toString());
                    startActivity(i);
                }else{
                   txterror.setError("Enter Roll No.");
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData() != null){
            filePath = data.getData();
            if(filePath != null) {
                fileobj = new FileOP(account_setup_profile.this);
                Glide.with(this)
                        .load(filePath)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imgProfilepic);
                filename.setText(fileobj.getFileName(filePath).toString());
            }
            else{
                Toast.makeText(account_setup_profile.this,"File Load error",Toast.LENGTH_LONG).show();
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
        imgProfilepic.setBackgroundColor(Color.TRANSPARENT);
        // imgUpload.setScaleType(ImageView.ScaleType.FIT_XY);
        chooseFile();
    }
}

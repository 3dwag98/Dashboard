package com.example.admin.noticeapp2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import static android.app.Activity.RESULT_OK;

public class Dialog extends DialogFragment {

    private static final int IMAGE_REQUEST_CODE = 123;

    public interface DialogListener{
        void onFinishEditDialog(String inputText,String tag);
    }
    public interface Listener{
        void onDismiss();
    }

    String[] list = {"ALL","FY","SY","TY"};
    String tag;
    Spinner sp ;
    EditText des;
    Button btn;
    TextView filename;
    ImageView attach;
    private Uri filePath ;
    Context context;
    String fname,type;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_layout,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp = view.findViewById(R.id.dropdown);
        des = view.findViewById(R.id.descrp);
        btn = view.findViewById(R.id.send);
        filename = view.findViewById(R.id.filename);
        attach = view.findViewById(R.id.attach);
        this.context = view.getContext();
        ArrayAdapter ad;
        ad = new ArrayAdapter(view.getContext(),android.R.layout.simple_spinner_item,list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(ad);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tag = list[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Snackbar.make(view,"SELECT ",Snackbar.LENGTH_LONG).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogListener dl = (DialogListener) getActivity();
                String desp = des.getText().toString();
                if(desp.isEmpty() || desp.equalsIgnoreCase("")){desp="";}
                dl.onFinishEditDialog(desp,tag);



                if(filePath != null) {
                    final ProgressDialog progress = new ProgressDialog(view.getContext());
                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progress.setTitle("File Uploading");
                    progress.setProgress(0);
                    progress.show();
                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                    Bundle args = getArguments();
                    final String child = args.getString("child");
                    final StorageReference mfile= mStorageRef.child("NotesUploads").child(child);
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
                                            db.child("Notes").child(child).child("type").setValue(type);
                                            db.child("Notes").child(child)
                                                    .child("upload").setValue(url)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(context, "Success...", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.v("Upload:  ",e+"");
                                            Toast.makeText(context, "Url :"+e, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "File upload unsuccessful..", Toast.LENGTH_SHORT).show();
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
                Listener l = (Listener) getActivity();
                l.onDismiss();
                dismiss();
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attach();
            }
        });
    }

    private void Attach() {
        chooseFile();

    }
    private void chooseFile() {
        Intent i = new Intent();
        i.setType("*/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Image"),IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData() != null){
            filePath = data.getData();
            FileOP obj = new FileOP(context);
            this.fname = obj.getFileName(filePath);
            this.type = obj.GetFileExtension(filePath);
            filename.setText(fname);
        }
    }

}

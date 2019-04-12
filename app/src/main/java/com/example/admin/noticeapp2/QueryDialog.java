package com.example.admin.noticeapp2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class QueryDialog extends DialogFragment {

    TextView txtTo;
    EditText query;
    Button btnSubmit;
    public interface DialogListener{
        void onDismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_query,container,false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTo = view.findViewById(R.id.to);
        query = view.findViewById(R.id.txtQuery);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        final Bundle args = getArguments();
        txtTo.setText("TO: "+args.getString("TO"));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String date = String.valueOf((new Date().getTime()));

                String to = args.getString("TO");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String from =user.getDisplayName();
                String  querystr = query.getText().toString();
                Query query1 =  new Query(to,from,querystr, Calendar.getInstance().getTime());
                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Query");
                db1.child(date).setValue(query1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(),"Submitted Query..",Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(),"Failed ."+e.toString(),Toast.LENGTH_LONG).show();
                        Log.e("EROR",e.toString());
                        dismiss();
                    }
                });

            }
        });
    }
}

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class FeedbackDialog extends DialogFragment {

    TextView txtfrom;
    EditText feedback;
    Button btnSubmit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_feedback,container,false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtfrom = view.findViewById(R.id.from);
        feedback = view.findViewById(R.id.txtfeedback);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        final Bundle args = getArguments();
        txtfrom.setText(args.getString("query"));
        final String date = String.valueOf((new Date().getTime()));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String from =user.getDisplayName();
                Query query1 =  new Query(args.getString("from"),from,args.getString("query"),feedback.getText().toString(), Calendar.getInstance().getTime());
                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Feedback");
                db1.child(date).setValue(query1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(),"Query Resolved",Toast.LENGTH_LONG);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(),"Error "+e.toString(),Toast.LENGTH_LONG);
                        Log.e("FeedbackError",e.toString());
                    }
                });
                DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Query");
               db2.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      for (DataSnapshot child: dataSnapshot.getChildren()){
                         if(child.getValue(Query.class).getDate().toString().equals(args.getString("time"))){
                             child.getRef().removeValue();
                             Toast.makeText(view.getContext(),"Submitted ",Toast.LENGTH_LONG).show();
                         }
                      }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
                dismiss();
            }
        });
    }
}
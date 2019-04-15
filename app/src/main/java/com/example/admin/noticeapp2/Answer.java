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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Answer extends DialogFragment {

    TextView query,feedback,from;
    Button btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_answer,container,false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedback = view.findViewById(R.id.feedback);
        query = view.findViewById(R.id.query);
        btn = view.findViewById(R.id.btnSubmit);
        from = view.findViewById(R.id.from);
        final Bundle args = getArguments();

        from.setText("From: "+args.getString("from"));
        feedback.setText(args.getString("feedback"));
        query.setText(args.getString("query"));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Feedback");
                db2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child :dataSnapshot.getChildren()){
                            if(child.getValue(Query.class).getDate().toString().equals(args.getString("time"))){
                                child.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(),"Deleted ",Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(),"Failed ."+e.toString(),Toast.LENGTH_LONG).show();
                                        Log.e("EROR",e.toString());
                                    }
                                });
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

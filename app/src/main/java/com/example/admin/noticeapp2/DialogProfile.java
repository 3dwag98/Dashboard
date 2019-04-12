package com.example.admin.noticeapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

public class DialogProfile extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_profile,container,false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle args = getArguments();

        ImageView profile = view.findViewById(R.id.profile);
        TextView email =view.findViewById(R.id.email);
        TextView uname=view.findViewById(R.id.uname);

        if(args.getString("file") != null){
            Glide.with(this)
                    .load(args.getString("file"))
                    .apply(RequestOptions.circleCropTransform())
                    .into(profile);
        }
        email.setText(args.getString("email").toString());
        uname.setText(args.getString("name").toString());
        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(view.getContext(),Login_Window.class));
                dismiss();
            }
        });

    }

}

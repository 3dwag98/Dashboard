package com.example.admin.noticeapp2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class RegisterDialog extends DialogFragment {

    public interface DialogListener{
        void onFinishEditDialog(String email,String pass);
        void Dismiss();
    }
    EditText email,pass;
    Button btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dilog_register,container,false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.txtEmail);
        pass = view.findViewById(R.id.txtPassword);
        btn  = view.findViewById(R.id.btnRegister);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogListener dl =(RegisterDialog.DialogListener) getActivity();
                dl.onFinishEditDialog(email.getText().toString(),pass.getText().toString());
                dl.Dismiss();
                dismiss();
            }
        });

    }

}

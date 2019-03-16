package com.example.admin.noticeapp2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DbAdapter extends FirebaseRecyclerAdapter<Notice,DbAdapter.NoticeHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context c;
    public DbAdapter(@NonNull FirebaseRecyclerOptions<Notice> options, Context c) {
        super(options);
        this.c = c;

    }

    @Override
    protected void onBindViewHolder(@NonNull DbAdapter.NoticeHolder holder, final int position, @NonNull Notice model) {
        holder.setTxtTitle(model.getTitle());
        holder.setTxtDesc(model.getDescrp());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(c,"heelo",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public DbAdapter.NoticeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        return new DbAdapter.NoticeHolder(view);
    }

    public void DeleteItem(int position) {
        getSnapshots().getSnapshot(position).getRef().removeValue();
        Notice n = getSnapshots().getSnapshot(position).getValue(Notice.class);
        String url = n.getUpload();

        try {
            StorageReference mref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            mref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(c,"deleted ...",Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.v("dataFail:  ",""+e);
                    Toast.makeText(c,""+e,Toast.LENGTH_SHORT).show();
                }
            })
            ;
        }
        catch(Exception ex){
           Log.v("dataError:  ",""+ex);
            Toast.makeText(c,""+ex,Toast.LENGTH_SHORT).show();
        }

        notifyDataSetChanged();
    }



    public class NoticeHolder extends RecyclerView.ViewHolder {

        public RelativeLayout root;
        public TextView txtTitle;
        public TextView txtDesc;

        public NoticeHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.item_noticetitle);
            txtDesc = itemView.findViewById(R.id.item_detail);
        }

        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }

        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }




    }

    @Override
    public void startListening() {
        super.startListening();

    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DataSnapshot snapshot, int newIndex, int oldIndex) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    @NonNull
    @Override
    public ObservableSnapshotArray<Notice> getSnapshots() {
        return super.getSnapshots();
    }
}

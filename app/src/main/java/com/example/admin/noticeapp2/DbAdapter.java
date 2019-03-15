package com.example.admin.noticeapp2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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
        holder.setTxtDesc(model.getDes());

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
       // getSnapshots().getSnapshot(position).getRef().removeValue();

        notifyItemRemoved(position);
        //notifyDataSetChanged();
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
}

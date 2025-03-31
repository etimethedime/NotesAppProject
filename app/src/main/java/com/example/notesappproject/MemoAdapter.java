package com.example.notesappproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter {
    private final ArrayList<Memo> memoData;
    private boolean isDeleting;
    private Context parentContext;


    private View.OnClickListener mOnItemClickListener;



    public class MemoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textDate;
        public Button deleteButton;


        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textNoteTitle);
            textDate = itemView.findViewById(R.id.textDatePublished);
            deleteButton = itemView.findViewById(R.id.buttonDeleteNote);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getTextViewTitle() {
            return textViewTitle;
        }

        public TextView getTextDate() {
            return textDate;
        }
        public Button getDeleteButton(){
            return deleteButton;
        }
    }


    public MemoAdapter(ArrayList<Memo> arrayList, Context context) {
        this.memoData = arrayList;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

@NonNull
@Override
public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item,
            parent, false);
    return new MemoViewHolder(v);
}

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MemoViewHolder cvh = (MemoViewHolder) holder;
        cvh.getTextViewTitle().setText(memoData.get(position).getTextViewTitle());
        cvh.getTextDate().setText(memoData.get(position).getTextDate());
        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }
    public void setDelete(boolean b) {
        isDeleting = b;
    }
    private void deleteItem(int position) {
        Memo memo = memoData.get(position);
        MemoDBSource ds = new MemoDBSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteMemo(memo.getId());
            ds.close();
            if (didDelete) {
                memoData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }
`

    @Override
    public int getItemCount() {
        return memoData.size();
    }

}

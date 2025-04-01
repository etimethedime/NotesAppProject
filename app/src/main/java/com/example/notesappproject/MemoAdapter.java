package com.example.notesappproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
    private ArrayList<Memo> memoData;
    private boolean isDeleting;
    private Context parentContext;


    private View.OnClickListener mOnItemClickListener;

    public class MemoViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textDate;
        public TextView textPriority;
        public Button deleteButton;


        public MemoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textNoteTitle);
            textDate = itemView.findViewById(R.id.textDatePublished);
            textPriority = itemView.findViewById(R.id.textPriority);
            deleteButton = itemView.findViewById(R.id.buttonDeleteNote);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }
    public MemoAdapter(ArrayList<Memo>memoData,Context context) {
        this.memoData= memoData;
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
//
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MemoViewHolder cvh = (MemoViewHolder) holder;
        cvh.textViewTitle.setText(memoData.get(position).getTitle());
        cvh.textDate.setText(memoData.get(position).getDate());
        String priorityText;
        int priority = memoData.get(position).getPriority();
        Log.d("MemoAdapter", "Priority value: " + priority);
        if (priority == 2) {
            priorityText = "High";
        } else if (priority == 1) {
            priorityText = "Medium";
        } else if (priority == 0) {
            priorityText = "Low";
        } else {
            priorityText = "Unknown";
        }

        cvh.textPriority.setText(priorityText);
        if (isDeleting) {
            cvh.deleteButton.setVisibility(View.VISIBLE);
            cvh.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = cvh.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        deleteItem(currentPosition);
                    }
                }
            });
        }
        else {
            cvh.deleteButton.setVisibility(View.INVISIBLE);        }
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

    @Override
    public int getItemCount() {
        return memoData.size();
    }
    public void setDelete(boolean b) {
        isDeleting = b;
    }

}

package com.lilian.firestore.firestoretest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReViewHolder> {
    private String[] dataset;

    public static class ReViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ReViewHolder(final View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.re_msg_text);
        }
    }

    public RemindersAdapter(String[] ds){
        dataset = ds;
    }

    @Override
    public RemindersAdapter.ReViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("Reminders Adapter", Integer.toString(viewType));
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        ReViewHolder vh = new ReViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ReViewHolder holder, int position){
        holder.mTextView.setText(dataset[position]);
    }

    @Override
    public int getItemCount(){
        return dataset.length;
    }

    @Override
    public int getItemViewType(final int position){
        return R.layout.reminders_card;
    }
}

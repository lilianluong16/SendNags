package com.lilian.firestore.firestoretest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ReViewHolder> {
    private String[] dataset;
    private static final String TAG = "FriendsListAdapter";

    public static class ReViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public ReViewHolder(final View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.friends_text);
            itemView.findViewById(R.id.button_accept).setOnClickListener(this);
            itemView.findViewById(R.id.button_decline).setOnClickListener(this);
        }


            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button_accept) {

                    Log.d(TAG, "accept " + String.valueOf(getAdapterPosition()));
                }
                if (v.getId() == R.id.button_decline) {

                    Log.d(TAG, "decline " + String.valueOf(getAdapterPosition()));
                }

            }


    }

    public FriendsListAdapter(String[] ds){
        dataset = ds;
    }

    @Override
    public FriendsListAdapter.ReViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("Friends List Adapter", Integer.toString(viewType));
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
        return R.layout.friends_list_card;
    }
}
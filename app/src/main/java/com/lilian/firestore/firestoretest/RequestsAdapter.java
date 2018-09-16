package com.lilian.firestore.firestoretest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ReViewHolder> {
    public static String[] dataset;
    private static final String TAG = "RequestsAdapter";

    public static class ReViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public AddFriendsActivity addFriendsActivity = new AddFriendsActivity();
        public ReViewHolder(final View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.friends_text);
            itemView.findViewById(R.id.button_accept).setOnClickListener(this);
            itemView.findViewById(R.id.button_decline).setOnClickListener(this);
        }


            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button_accept) {

                    Log.d(TAG, "accept " + dataset[getAdapterPosition()]);
                    addFriendsActivity.addFriend(""+dataset[getAdapterPosition()]);
                    addFriendsActivity.deleteRequest(""+dataset[getAdapterPosition()]);
                }
                if (v.getId() == R.id.button_decline) {

                    Log.d(TAG, "decline " + String.valueOf(getAdapterPosition()));
                    addFriendsActivity.deleteRequest(""+dataset[getAdapterPosition()]);
                }

            }


    }

    public RequestsAdapter(String[] ds){
        dataset = ds;
    }

    @Override
    public RequestsAdapter.ReViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
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
        return R.layout.requests_card;
    }
}
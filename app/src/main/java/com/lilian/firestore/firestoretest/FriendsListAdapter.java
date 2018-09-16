package com.lilian.firestore.firestoretest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ReViewHolder> {
    public static String[] dataset;
    private static final String TAG = "FriendsListAdapter";


    public static class ReViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public FriendsListActivity friendsListActivity = new FriendsListActivity();
        private FirebaseFirestore db;
        private FirebaseUser mUser;
        public ReViewHolder(final View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.friends_text);
            itemView.findViewById(R.id.button_delete).setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_delete) {
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                db = FirebaseFirestore.getInstance();
                friendsListActivity = new FriendsListActivity();
                Log.d(TAG, "delete " + dataset[getAdapterPosition()]);
                // delete Friend from list
                friendsListActivity.deleteFriend(""+dataset[getAdapterPosition()]);

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
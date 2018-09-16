package com.lilian.firestore.firestoretest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.ReViewHolder> {
    private ArrayList<String> messages, senders, times;
    private FirebaseUser mUser;

    private FirebaseFirestore db;

    public static class ReViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ReViewHolder(final View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.re_msg_text);
        }
    }

    public RemindersAdapter(ArrayList<String> msgs, ArrayList<String> sdrs, ArrayList<String> tms){
        messages = msgs;
        senders = sdrs;
        times = tms;
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
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        final ReViewHolder holder1 = holder;
        final int position1 = position;
        db.collection("users")
                .whereEqualTo("email", senders.get(position1))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                holder1.mTextView.setText(document.get("name") + " reminds you to " + messages.get(position1)
                                        + " at " + times.get(position1));
                                break;
                            }
                        } else {

                        }
                    }
                });

    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    @Override
    public int getItemViewType(final int position){
        return R.layout.reminders_card;
    }
}

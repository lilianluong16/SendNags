package com.lilian.firestore.firestoretest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RemindersActivity extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser mUser;
    private String TAG = "Reminders";
    private FirebaseFirestore db;
    public NotificationMgr nmgr;

    static RemindersActivity newInstance(int num) {
        RemindersActivity f = new RemindersActivity();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_reminders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        final View v = view;
        db = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.reminders_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        String email = mUser.getEmail();

        db.collection("users")
                .document(email).collection("reminders")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if(e != null){
                            Log.w("main", "listen:error", e);
                            return;
                        }

                        ArrayList<String> messages = new ArrayList<String>();
                        ArrayList<String> senders = new ArrayList<String>();
                        ArrayList<String> times = new ArrayList<String>();
                        for(DocumentSnapshot document: queryDocumentSnapshots){
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            messages.add(document.getData().get("message").toString());
                            senders.add(document.getData().get("sender").toString());
                            times.add(String.format("%02d", document.get("hour")) + ":" + String.format("%02d", document.get("minute")));
                        }

                        mAdapter = new RemindersAdapter(messages, senders, times);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });

    }
}

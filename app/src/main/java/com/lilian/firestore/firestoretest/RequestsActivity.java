package com.lilian.firestore.firestoretest;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestoreException;

public class RequestsActivity extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser mUser;

    private FirebaseFirestore db;
    private static final String TAG = "RequestsActivity";
    ArrayList<String> friends = new ArrayList<String>();

    static RequestsActivity newInstance(int num) {
        RequestsActivity f = new RequestsActivity();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_requests, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        final View v = view;
        Log.d("Requests", "the view has been created!!!");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("users").document(mUser.getEmail());

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                mRecyclerView = getView().findViewById(R.id.friends_recycler_view);
                LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(layoutManager);
                friends = new ArrayList<String>();
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    if ((ArrayList<String>)snapshot.getData().get("requests")!=null) {
                        for(String friend:(ArrayList<String>)snapshot.getData().get("requests")) {
                            Log.d(TAG, "adding " + friend);
                            friends.add(friend);
                        }

                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
                String[] list = new String[friends.size()];

                for(int i=0; i<friends.size(); i++) {
                    list[i] = friends.get(i);
                }
                mAdapter = new RequestsAdapter(list);
                mRecyclerView.setAdapter(mAdapter);
            }

        });


    }
}

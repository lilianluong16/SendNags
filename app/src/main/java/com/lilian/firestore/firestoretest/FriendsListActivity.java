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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestoreException;

public class FriendsListActivity extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser mUser;

    private FirebaseFirestore db;
    private static final String TAG = "FriendsListActivity";
    public ArrayList<String> friends = new ArrayList<String>();

    static FriendsListActivity newInstance(int num) {
        FriendsListActivity f = new FriendsListActivity();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_friends_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        final View v = view;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mRecyclerView = getView().findViewById(R.id.friends_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        final DocumentReference docRef = db.collection("users").document(mUser.getEmail());

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                friends = new ArrayList<String>();
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    if ((ArrayList<String>) snapshot.getData().get("friends") != null) {
                        for (String friend : (ArrayList<String>) snapshot.getData().get("friends")) {
                            Log.d(TAG, "adding " + friend);
                            friends.add(friend);
                        }

                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
                String[] list = new String[friends.size()];

                for (int i = 0; i < friends.size(); i++) {
                    list[i] = friends.get(i);
                }
                mAdapter = new FriendsListAdapter(list);
                mRecyclerView.setAdapter(mAdapter);
            }


        });

    }
    public void deleteFriend(String email) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // go into users, go into that person, go into "friends" array, add email of friend
        db.collection("users").document(mUser.getEmail())
                .update("friends", FieldValue.arrayRemove(email));
        db.collection("users").document(email)
                .update("friends", FieldValue.arrayRemove(mUser.getEmail()));
    }
}

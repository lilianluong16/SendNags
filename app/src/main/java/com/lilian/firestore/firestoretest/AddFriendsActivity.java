package com.lilian.firestore.firestoretest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFriendsActivity extends Fragment implements View.OnClickListener {

    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private static final String TAG = "AddFriendsActivity";

    static AddFriendsActivity newInstance(int num) {
        AddFriendsActivity f = new AddFriendsActivity();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.activity_add_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        final View v = view;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        getView().findViewById(R.id.button_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button_send:
                String target = sendRequest();
                break;
            // ...
        }
    }

    private String sendRequest() {
        EditText friend = (EditText) getView().findViewById(R.id.edittext_email);
        final String email = friend.getText().toString();

        final DocumentReference userRef = db.collection("users").document(email);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userRef
                                .update("requests", FieldValue.arrayUnion(mUser.getEmail()));
                        // send request

                    } else {
                        Log.d(TAG, "No such person");
                        // display this person doesnt exist
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return email;
    }

    public String deleteRequest(String email) {
        final String emailFriend = email;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        final DocumentReference userRef = db.collection("users").document(mUser.getEmail());
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userRef
                                .update("requests", FieldValue.arrayRemove(emailFriend));
                        // send request

                    } else {
                        Log.d(TAG, "No such person");
                        // display this person doesnt exist
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return email;
    }

    public void addFriend(String email){

        //user.put("email", mUser.getEmail());

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // go into users, go into that person, go into "friends" array, add email of friend
        db.collection("users").document(mUser.getEmail())
                .update("friends", FieldValue.arrayUnion(email));
        db.collection("users").document(email)
                .update("friends", FieldValue.arrayUnion(mUser.getEmail()));
    }
}

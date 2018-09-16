package com.lilian.firestore.firestoretest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class AddFriendsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private static final String TAG = "AddFriendsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "NEW ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.button_send).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button_send:
                String target = sendRequest();
                // return whether other person says yes
                // addFriend(target);
                break;
            // ...
        }
    }

    private String sendRequest() {
        EditText friend = (EditText) findViewById(R.id.edittext_email);
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
                                .update("requests", FieldValue.arrayUnion(mUser.getDisplayName()));
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
    private void addFriend(String email){

        //user.put("email", mUser.getEmail());

        // go into users, go into that person, go into "friends" array, add email of friend
        db.collection("users").document(mUser.getEmail())
                .update("friends", FieldValue.arrayUnion(email));
    }
}

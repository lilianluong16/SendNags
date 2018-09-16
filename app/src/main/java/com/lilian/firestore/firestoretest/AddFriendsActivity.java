package com.lilian.firestore.firestoretest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

public class AddFriendsActivity extends NavActivity implements View.OnClickListener {

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
                // send friend request
                // if other person says yes
                addFriend();
                break;
            // ...
        }
    }

    private void addFriend(){
        EditText friend = (EditText) findViewById(R.id.edittext_email);
        String email = friend.getText().toString();

        //user.put("email", mUser.getEmail());

        // go into users, go into that person, go into "friends" array, add email of friend
        db.collection("users").document(mUser.getEmail())
                .update("friends", FieldValue.arrayUnion(email));
    }
}
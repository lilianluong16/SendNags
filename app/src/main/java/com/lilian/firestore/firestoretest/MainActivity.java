package com.lilian.firestore.firestoretest;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.button_send).setOnClickListener(this);

        // Get data
        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null){
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        List<String> users = new ArrayList<>();
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            if(doc.get("name") != null && doc.get("email") != null){
                                users.add(doc.getString("") + " " + doc.getString("last"));
                            }
                        }
                        TextView dt = (TextView) findViewById(R.id.text_display);
                        dt.setText("All users: " + users);
                    }
                });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button_send:
                send();
                break;
            // ...
        }
    }

    private void send(){
        Map<String, Object> user = new HashMap<>();
        EditText friend = (EditText) findViewById(R.id.edittext_email);
        String email = friend.getText().toString();

        //user.put("email", mUser.getEmail());

        // go into users, go into that person, go into "friends" array, add email of friend
        db.collection("users").document(mUser.getEmail())
                .update("friends", FieldValue.arrayUnion(email))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}

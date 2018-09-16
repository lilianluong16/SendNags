package com.lilian.firestore.firestoretest;

import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {
    private TimePickerDialog timePickerDialog;
    private TextView timeDisplay;
    private AutoCompleteTextView friendInput;
    private int hour, minute;
    private String TAG = "SendActivity";
    private ArrayList<String> listFriends;
    private FirebaseFirestore db;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        hour = 12;
        Button setTimeButton = (Button) findViewById(R.id.send_time_button);
        Button submitButton = (Button) findViewById(R.id.send_nag_button);
        timeDisplay = (TextView) findViewById(R.id.send_time_display);
        friendInput = (AutoCompleteTextView) findViewById(R.id.input_target);
        setTimeButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        listFriends = new ArrayList<String>();
        db = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(mUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        ArrayList<String> fs = (ArrayList<String>) document.getData().get("friends");
                        Log.v(TAG, fs.toString());
                        for (String f : fs) {
                            db.collection("users").document(f).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot doc = task.getResult();
                                                Log.d(TAG, doc.get("email") + " found " + doc.get("name"));
                                                listFriends.add((String) doc.get("name"));
                                            }
                                            Log.d(TAG, listFriends.toString());
                                            ArrayAdapter<String> adapter =
                                                    new ArrayAdapter<String>(SendActivity.this, android.R.layout.simple_list_item_1, listFriends);
                                            friendInput.setAdapter(adapter);
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.send_time_button:
                setTime();
                break;
            case R.id.send_nag_button:
                sendNag();
                break;
        }
    }

    private void setTime(){
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute_a) {
                hour = hourOfDay;
                minute = minute_a;
                timeDisplay.setText("For " + String.format("%02d", hour) + ":" + String.format("%02d", minute));
            }
        }, 0, 0, false);
        timePickerDialog.show();
    }

    private void sendNag(){
        db.collection("users")
                .whereEqualTo("name", ((AutoCompleteTextView) findViewById(R.id.input_target)).getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                Map<String, Object> rem = new HashMap<>();
                                rem.put("sender", mUser.getEmail());
                                rem.put("message", ((EditText) findViewById(R.id.input_message)).getText().toString());
                                rem.put("time_type", "time");
                                rem.put("hour", hour);
                                rem.put("minute", minute);
                                String target_email = (String)document.getData().get("email");
                                db.collection("users").document(target_email)
                                        .collection("reminders")
                                        .add(rem);
                                break;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}

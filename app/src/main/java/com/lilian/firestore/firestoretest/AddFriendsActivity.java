package com.lilian.firestore.firestoretest;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

public class AddFriendsActivity extends NavActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private static final String TAG = "AddFriendsActivity";
//    private TextView name, email;
//    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "NEW ACTIVITY");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        findViewById(R.id.button_send).setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        name = (TextView) findViewById(R.id.name);
//        email = (TextView) findViewById(R.id.email);
//        photo = (ImageView) findViewById(R.id.photo);
//        name.setText(mUser.getDisplayName());
//        email.setText(mUser.getEmail());
//        String photoUrl = mUser.getPhotoUrl().toString();
//        photo.setImageResource(getResources().getIdentifier(photoUrl,null,null));
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

package com.lilian.firestore.firestoretest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nullable;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser mUser;
    private FirebaseFirestore db;
    public NotificationMgr nmgr;
    private String TAG = "NavActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        nmgr = new NotificationMgr(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SendNags");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = null;
        Class fragmentClass = RemindersActivity.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        //ALaRMS
        db = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference colref = db.collection("users").document(mUser.getEmail())
                .collection("reminders");
        colref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        int rc = document.getId().substring(0, 6).hashCode();
                        Long hour = (long) document.get("hour");
                        Long minute = (long) document.get("minute");
                        Log.d(TAG, String.valueOf(nmgr.checkAlarm(document.get("message").toString(),
                                document.get("sender").toString(),
                                (int)hour.intValue(),
                                (int)minute.intValue(),
                                rc)));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        colref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Log.w("main", "listen:error", e);
                    return;
                }

                for(DocumentChange dc: queryDocumentSnapshots.getDocumentChanges()){
                    int rc = dc.getDocument().getId().substring(0, 6).hashCode();
                    Long hour = (long) dc.getDocument().get("hour");
                    Long minute = (long) dc.getDocument().get("minute");
                    switch(dc.getType()){
                        case ADDED:
                            Log.d("main", "alarm set");
                            nmgr.addAlarm(dc.getDocument().get("message").toString(),
                                    dc.getDocument().get("sender").toString(),
                                    (int)hour.intValue(),
                                    (int)minute.intValue(),
                                    rc);
                            break;
                        case REMOVED:
                            Log.d("main", "alarm cancel");
                            nmgr.cancelAlarm(dc.getDocument().get("message").toString(),
                                    dc.getDocument().get("sender").toString(),
                                    (int)hour.intValue(),
                                    (int)minute.intValue(),
                                    rc);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView) findViewById(R.id.nav_header_name)).setText(mUser.getDisplayName());
        ((TextView) findViewById(R.id.nav_header_email)).setText(mUser.getEmail());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = RemindersActivity.class;
        if (id == R.id.nav_sendrem) {
            // Handle the camera action
            fragmentClass = SendActivity.class;
        } else if (id == R.id.nav_reminders) {
            fragmentClass = RemindersActivity.class;
        } else if (id == R.id.nav_friends_list) {
            fragmentClass = FriendsListActivity.class;
        } else if (id == R.id.nav_friend_requests) {
            fragmentClass = RequestsActivity.class;
        } else if (id == R.id.nav_add_friend) {
            fragmentClass = AddFriendsActivity.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package com.lilian.firestore.firestoretest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //alarm Manager
    int hour;
    int minute;
    public static String title = "SendNags";
    public static String message = "LOL";

    EditText hour_a;
    EditText minute_a;
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmtestapi);
        this.context = this;
//input
        hour_a = (EditText) findViewById(R.id.hour_a);
        minute_a = (EditText) findViewById(R.id.minute_a);


        //initialize alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //initial ize update text
        update_text = (TextView) findViewById(R.id.update_text);

        //calendar
        final Calendar calendar = Calendar.getInstance();

        //initialize buttons
        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        //initialize stop
        Button alarm_off = (Button) findViewById(R.id.alarm_off);
        //create intent to Alarm Receiver
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);


        //create onclick listener on
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = Integer.valueOf(hour_a.getText().toString());
                minute = Integer.valueOf(minute_a.getText().toString());

//set calendar instance with hour and minute
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                //string of hour and minute
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);



                //update txt
                set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);

                //pending intent, delays intent till time
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                //alarmmanager setting
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });

        //onclick off
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //method change update txt
                set_alarm_text("Alarm off!");


            }
        });


    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }
}


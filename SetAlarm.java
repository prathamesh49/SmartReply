package com.guru49.connectit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class SetAlarm extends AppCompatActivity {


    TimePicker timePicker ;
    Spinner intervaldays ;
    ArrayList<String> days ;
    ArrayAdapter <String> daysadapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        timePicker = findViewById(R.id.timepick);
        Button savetime = findViewById(R.id.savetimebtn);
        intervaldays = findViewById(R.id.intervalspinner);
        days = new ArrayList<>() ;
        days.add("1 days") ;
        days.add("2 days") ;
        days.add("4 days") ;
        days.add("7 days") ;

        daysadapter = new ArrayAdapter<>(SetAlarm.this, android.R.layout.simple_spinner_dropdown_item , days) ;
        intervaldays.setAdapter(daysadapter) ;


        savetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String selectedinterval = intervaldays.getSelectedItem().toString() ;


                Calendar calendar = Calendar.getInstance();

                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                } else {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                }


                setAlarm(calendar.getTimeInMillis());
            }

        });

    }

    private void setAlarm(long timeInMillis) {

        String selectedinter = intervaldays.getSelectedItem().toString() ;

        if (selectedinter.equals("1 days")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, Myalarm.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis , AlarmManager.INTERVAL_DAY ,  pendingIntent);

            Toast.makeText(this, " Interval set for " + selectedinter ,  Toast.LENGTH_SHORT).show() ;
            onBackPressed() ;

        }
        else if ( selectedinter.equals("2 days")) {

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, Myalarm.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, 172800000, pendingIntent);
            Toast.makeText(this, "Interval set for " + selectedinter , Toast.LENGTH_SHORT).show();

            onBackPressed();

        }

        else if (selectedinter.equals("4 days")) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, Myalarm.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, 345600000, pendingIntent);
            Toast.makeText(this, " Interval set for " + selectedinter , Toast.LENGTH_SHORT).show();
            onBackPressed();

        }

        else  {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, Myalarm.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis , 604800000, pendingIntent);
            Toast.makeText(this, " Interval set for " + selectedinter , Toast.LENGTH_SHORT).show() ;
            onBackPressed() ;

        }



    }


}

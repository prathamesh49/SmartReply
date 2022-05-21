package com.guru49.connectit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.widget.Toast;

public class Myalarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences ("connect" , Context.MODE_PRIVATE  ) ;
        SharedPreferences.Editor editor = sharedPreferences.edit() ;
        editor.putString ("phonenumber" , "" ) ;
        editor.apply() ;

        //Toast.makeText( context.getApplicationContext()  , "alarm" , Toast.LENGTH_LONG) .show() ;
    }
}



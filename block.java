package com.guru49.connectit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class block extends AppCompatActivity  {


    TextView blocked , blocknumber ;
    Button block , unblock ;

    ArrayList <String> wordarray = new ArrayList<String>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar() ;
        actionBar.setBackgroundDrawable(getResources().getDrawable( R.drawable  .color     ));

        blocked = findViewById(R.id.blockedcontacts);
        blocknumber = findViewById(R.id.block_number4) ;
        block = findViewById( R.id.block3 ) ;
        unblock = findViewById(R.id.unblock3 ) ;



        SharedPreferences sharedPreferences = getSharedPreferences("connect", MODE_PRIVATE);
        String bl = sharedPreferences.getString("blocked", "");

        for (String words : bl.split(" ")) {
            wordarray.add(words);

        }

        for ( int i=0 ; i< wordarray.size () ; i++ ) {
            String al = blocked.getText().toString() ;

            blocked.setText(al +"\n" +wordarray.get( i ));
        }





        unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences spb = getSharedPreferences("connect" , MODE_PRIVATE  ) ;
                String already = spb .getString("blocked" , "" ) ;
                if (blocknumber.getText().toString().isEmpty()) {
                    blocknumber.setError( "field can't be empty")  ;
                }
                else if ( ! already.contains(blocknumber .getText().toString() )) {
                    Toast.makeText(getApplicationContext() , "number is not in your block list " , Toast.LENGTH_LONG) .show();
                }
                else {

                    String newblock = already.replaceAll (blocknumber .getText().toString() , "") ;
                    SharedPreferences .Editor editor = spb.edit() ;
                    editor.putString("blocked" , newblock ) ;
                    editor.apply() ;
                    blocknumber.setText( "");
                    Toast.makeText(getApplicationContext(), "number unblocked successfully..." , Toast.LENGTH_LONG ) .show() ;
                    onBackPressed() ;
                }
            }
        });



        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = getSharedPreferences ("connect" , MODE_PRIVATE ) ;
                String old_blocked_numbers = sp.getString("blocked" , "") ;
                SharedPreferences.Editor editor = sp.edit() ;

                if (blocknumber.getText().toString() .isEmpty()) {
                    blocknumber.setError("field can't be empty");

                }

                else  if (blocknumber.getText().toString().length() != 10 )                   {
                    blocknumber.setError("mobile number should be 10 digit");
                }

                else if ( old_blocked_numbers.contains( blocknumber.getText().toString() )) {
                    Toast.makeText(getApplicationContext() , "mobile number already blocked..." , Toast.LENGTH_LONG) .show() ;
                }

                else {
                    String new_blocked_number = old_blocked_numbers + " " + blocknumber.getText() .toString() ;
                    editor.putString(  "blocked", new_blocked_number) ;
                    editor .apply () ;

                    blocknumber.setText( "" ) ;
                    Toast.makeText(getApplicationContext() , "mobile number "  + blocknumber.getText().toString() + " successfully blocked... ", Toast.LENGTH_LONG).show() ;
                    onBackPressed() ;

                }
            }
        }) ;


    }
}


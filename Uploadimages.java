package com.guru49.connectit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.guru49.connectit.utility.NetworkChangeListener;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Uploadimages extends AppCompatActivity {

    EditText name, email  , phone, address, business ;
    TextView location ;
    Button submit ;
    ProgressDialog progressDialog ;

    FusedLocationProviderClient fusedLocationProviderClient ;

    ProgressDialog p ;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimages) ;

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar() ;
        actionBar.setBackgroundDrawable( getResources().getDrawable(  R.drawable.color ));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }




        name = findViewById( R.id.name) ;
        email = findViewById( R.id.mail) ;
        phone = findViewById( R.id.phone) ;
        address = findViewById( R.id.address) ;
        business = findViewById( R.id.business ) ;
        location = findViewById( R.id.getlocation) ;
        submit = findViewById( R.id.submit) ;

        SharedPreferences sharedPreferences = getSharedPreferences("connect" , MODE_PRIVATE) ;
        SharedPreferences sharedPreferences1 = getSharedPreferences("details" , MODE_PRIVATE) ;
        String username = sharedPreferences.getString("key_username", "") ;

        String cname = sharedPreferences1.getString("name", "") ;
        String cemail = sharedPreferences1.getString("email", "") ;
        String cphone = sharedPreferences1.getString("phone", "") ;
        String caddress = sharedPreferences1.getString("address", "") ;
        String  cbusiness = sharedPreferences1.getString("business", "") ;

        name.setText(cname) ;
        email.setText (cemail) ;
        phone .setText( cphone ) ;
        address.setText( caddress ) ;
        business.setText( cbusiness) ;

        String usernode = username.substring( 0, username.length() - 23  ) ;


        fusedLocationProviderClient = LocationServices .getFusedLocationProviderClient(this)  ;


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission( Uploadimages.this , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Uploadimages.this , new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 100);
                }
                else {
                    /*p = new ProgressDialog(Uploadimages.this);
                    p.show();
                    p.setContentView(R.layout.progressdialog);
                    p.setCancelable(true);
                    p.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                     */
                    getLocation();
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().isEmpty()) {
                    name.setError("please enter your name");
                }

               else if (email.getText().toString().isEmpty()) {
                    email.setError("please enter your email");
                }
                else if (address.getText().toString().isEmpty()) {
                    address.setError("please enter your address");
                }

                else if (phone.getText().toString().isEmpty()) {
                    phone.setError("please enter your mobile number");
                }
                else if ( business.getText().toString().isEmpty()) {
                    business.setError("please enter your business type");

                }

                else {

                    progressDialog = new ProgressDialog(Uploadimages.this) ;
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progressdialog);
                    progressDialog.setCancelable(false);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                    HashMap<String, Object> pq = new HashMap<>();
                    pq.put("name", name.getText().toString());
                    pq.put("mobile" , phone.getText().toString());
                    pq.put("address", address.getText().toString());
                    pq.put("email" , email.getText().toString());
                    pq.put("business", business.getText().toString() ) ;

                    FirebaseDatabase .getInstance().getReference().child("customers")
                            .child(usernode )
                            .updateChildren(pq)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task .isSuccessful()) {

                                        SharedPreferences sp = getSharedPreferences( "details" , MODE_PRIVATE) ;

                                        SharedPreferences.Editor editor = sp.edit() ;
                                        editor.putString("name", name.getText().toString()) ;
                                        editor.putString("email", email.getText().toString()) ;
                                        editor.putString("phone", phone.getText().toString()) ;
                                        editor.putString("address" , address.getText().toString()) ;
                                        editor.putString("business" , business.getText().toString()) ;
                                        editor.apply() ;
                                        progressDialog.dismiss() ;
                                        Toast.makeText(getApplicationContext(), "Data submitted successfully..." , Toast.LENGTH_LONG) .show() ;
                                        onBackPressed() ;
                                    }

                                    else  {
                                        progressDialog .dismiss() ;
                                        Toast.makeText(getApplicationContext() , "turn on the internet and try again..." , Toast.LENGTH_LONG) .show() ;
                                     }
                                }
                            }) ;

                }

            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult() ;
                if (location !=null) {
                    Geocoder geocoder = new Geocoder( Uploadimages.this, Locale.getDefault())  ;
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1) ;
                        address.setText(addresses.get(0).getAddressLine(0));



                    } catch (Exception e) {

                    }

                }
                else {

                    Toast toast = Toast.makeText(Uploadimages.this, "Please turn on the location and try again or enter the address manually", Toast.LENGTH_LONG) ;
                    toast.show();
                }

            }
        }) ;


    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION ) ;
        registerReceiver(networkChangeListener, filter)  ;
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }



}


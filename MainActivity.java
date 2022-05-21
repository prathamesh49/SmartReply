package com.guru49.connectit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.guru49.connectit.utility.NetworkChangeListener;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences ;
    public EditText useremail, userpassword ;
    private Button loginbutton ;
    private FirebaseAuth mAuth;
    public static String shared_pref_name = "connect" ;
    public  static  final String KEY_EMAIL= "key_username" ;
    ProgressDialog progressDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.color));


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }


        mAuth = FirebaseAuth.getInstance();
        useremail = findViewById(R.id.email);
        userpassword = findViewById(R.id.password);
        loginbutton = findViewById(R.id.btnlogin) ;
        String deviceid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID) ;
        sharedPreferences = getSharedPreferences(shared_pref_name, MODE_PRIVATE ) ;


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (useremail.getText().toString().isEmpty()) {
                    useremail.setError("Enter Email");
                    return;
                }
                else if (userpassword.getText().toString().isEmpty()) {
                    userpassword.setError("Enter Password");
                }

                else {

                    progressDialog = new ProgressDialog(MainActivity.this) ;
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progressdialog);
                    progressDialog.setCancelable(false);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    mAuth.signInWithEmailAndPassword(useremail.getText().toString().trim(), userpassword.getText().toString().trim()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                SharedPreferences.Editor editor = sharedPreferences.edit() ;
                                editor.putString(KEY_EMAIL, useremail.getText().toString().trim()) ;
                                editor.apply() ;
                                HashMap <String, String > hashMap = new HashMap<>() ;
                                hashMap.put("DeviceID" , deviceid) ;
                                hashMap.put("URL" , "www.triosolutionsindia.com" ) ;
                                FirebaseFirestore.getInstance() .collection("Users") .document(useremail.getText().toString().trim() )
                                        .set(hashMap )
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.e("guru", "Onsuccess : ") ;

                                            }
                                        }) .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText( getApplicationContext(), "error", Toast.LENGTH_LONG) .show() ;
                                    }
                                }) ;
                                Toast.makeText(MainActivity.this, "logged in", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss() ;
                                Intent intent = new Intent(MainActivity.this, defaultmessage.class) ;
                                startActivity(intent);
                                finish();



                            } else {
                                progressDialog.dismiss() ;
                                Toast.makeText(MainActivity.this, "Invalid Email and Password", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferences  =getSharedPreferences("connect", MODE_PRIVATE) ;
        String emptyornot = sharedPreferences.getString("key_username" , null) ;

        if (  emptyornot != null) {
            startActivity( new Intent(MainActivity.this, defaultmessage.class));
            finish() ;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.aboutus) {
            startActivity(new Intent(MainActivity.this, Aboutus.class))  ;
        }

        return super.onOptionsItemSelected(item);
    }
}


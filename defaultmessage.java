package com.guru49.connectit;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class defaultmessage extends AppCompatActivity {


    String deviceid ;
    String url ;

    TextView username ;
    CountDownTimer countDownTimer;
    public  static  final String TAG = "defaultmessage" ;
    CardView mydetails , mymessage , muuploadphotos , myblockunblock ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaultmessage);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.color)) ;


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1) ;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }

        username = findViewById(R.id.username)  ;

        mydetails = findViewById(R.id .mydetails) ;
        mymessage = findViewById( R.id.mydefultmessage) ;
        muuploadphotos = findViewById(R.id.myuploadimages) ;
        myblockunblock = findViewById( R.id.myblockunblock) ;

        mydetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext () , Uploadimages.class));
            }
        }) ;


        mymessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext () , setdefaultmessage.class));
            }
        }) ;

        muuploadphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext () , mydetails.class));
            }
        }) ;

        myblockunblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext () , block.class));
            }
        }) ;

        //String currentuser = FirebaseAuth.getInstance().getCurrentUser().getEmail() ;
        //username.setText(currentuser);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.shared_pref_name , MODE_PRIVATE) ;
        String useremailid = sharedPreferences.getString("key_username", null) ;
        username.setText(useremailid) ;

        FirebaseFirestore .getInstance().collection("Users").document(useremailid) .get()
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String url = documentSnapshot.get("URL").toString() ;
                SharedPreferences.Editor editor = sharedPreferences.edit() ;
                editor.putString("url", url) ;
                editor.apply() ;
            }
        }) ;


        deviceid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID) ;



        getloginstatus () ;



    }

    private void showAlertDialog() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("New Update Available  " )
                .setMessage("Update Now")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent (Intent.ACTION_VIEW , Uri.parse("https://play.google.com/store/apps/details?id=com.guru49.connectit")));
                        } catch ( Exception e) {
                            Toast.makeText(getApplicationContext(), "something went wrong" , Toast.LENGTH_SHORT) .show() ;
                        }
                    }
                }) .show() ;
        alertDialog.setCancelable(false);


    }

    private  int getcurrentversion () {
        PackageInfo packageInfo = null ;
        try {
            packageInfo = getPackageManager() .getPackageInfo (getPackageName() , 0) ;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("guru" , e.getMessage() ) ;

        }
        return  packageInfo.versionCode ;
    }



    private void getloginstatus() {
        countDownTimer = new CountDownTimer( 5000 , 1000 ) {
            @Override
            public void onTick(long l) {
                Log.e( TAG, "ontick : " +l/1000 ) ;
            }


            @Override
            public void onFinish() {
                countDownTimer.start();
                FirebaseFirestore.getInstance().collection("Users").document (username.getText().toString().trim())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String device =  documentSnapshot.get("DeviceID") .toString()   ;
                        url = documentSnapshot.get("URL") .toString() ;

                        if ( ! device.equals (deviceid))       {

                            SharedPreferences sharedPreferences = getSharedPreferences ("connect", MODE_PRIVATE) ;
                            SharedPreferences.Editor editor1 = sharedPreferences.edit() ;

                            editor1.clear() ;
                            editor1.apply() ;
                            FirebaseAuth.getInstance().signOut() ;
                            finish() ;

                            //showAlert("Logged in in another device ") ;

                        }
                    }
                }) ;
            }
        }.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.aboutus) {
            startActivity(new Intent(defaultmessage.this, Aboutus.class))  ;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        int currentversioncode = getcurrentversion() ;

        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance() ;
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1 ).build() ;

        firebaseRemoteConfig .setConfigSettingsAsync(configSettings) ;
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful())
                {
                    final String new_version_code = firebaseRemoteConfig .getString("new_version_code") ;

                    if (Integer.parseInt(new_version_code) > getcurrentversion()) {

                        showAlertDialog() ;
                    }
                }
            }
        }) ;
        super.onStart();
    }
}



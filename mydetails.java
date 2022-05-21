package com.guru49.connectit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guru49.connectit.utility.NetworkChangeListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class mydetails extends AppCompatActivity {

    Button uploadimg, selectimages ;
    TextView selected_images ;
    private Uri imageuri ;
    ArrayList<Uri> imagelist = new ArrayList<Uri>() ;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydetails);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.color)) ;

        selected_images = findViewById (R.id .selected_img)   ;
        selectimages = findViewById(R.id.select_images) ;
        uploadimg = findViewById(R.id.upload_image) ;

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.shared_pref_name , MODE_PRIVATE) ;
        String useremailid = sharedPreferences.getString("key_username", null) ;

        uploadimg.setVisibility(View.GONE);
        selected_images.setVisibility(View.GONE) ;
        selected_images.setVisibility(View.GONE) ;


        selectimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(mydetails.this  , new  String[] {Manifest.permission . READ_EXTERNAL_STORAGE } , 1)  ;
                }

                else {

                    imagelist.clear() ;


                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, 1);
                }



            }

        });


        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String userfolder = useremailid.substring(0 , useremailid.length() - 23) ;

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(userfolder) ;


                for (int uploadcount = 0; uploadcount < imagelist.size(); uploadcount++) {

                    Uri individualimage = imagelist.get(uploadcount);

                    String imgname = String.valueOf(uploadcount);


                    InputStream inputStream = null;
                    try {
                        inputStream = getContentResolver().openInputStream(individualimage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    StorageReference imagename = storageReference.child(imgname + ".jpg");

                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bmp.compress(Bitmap.CompressFormat.JPEG, 25, stream);

                    byte[] byteArray = stream.toByteArray();


                    imagename.putBytes(byteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "photo uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                uploadimg .setVisibility(View.GONE) ;
                selected_images .setVisibility(View.GONE) ;
                onBackPressed() ;
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK) {
            if (data.getClipData() !=null ) {

                int countclipdata = data.getClipData().getItemCount() ;

                if (countclipdata > 6 ) {

                    Toast.makeText( getApplicationContext(), " Please select upto 6 images " , Toast.LENGTH_LONG ) .show();
                }

                else  {
                    int currentimageselect = 0 ;
                    while (currentimageselect < countclipdata) {                           


                        imageuri = data.getClipData().getItemAt(currentimageselect).getUri();
                        imagelist.add(imageuri) ;
                        currentimageselect = currentimageselect + 1;

                    }



                    //selectimages .setVisibility ( View.GONE );
                    selected_images.setVisibility(View.VISIBLE) ;
                    selected_images. setText( "You have selected " + imagelist.size() + " images ");
                    uploadimg .setVisibility(View.VISIBLE);

                }

            } else  {
                Toast.makeText(getApplicationContext(), "please select more than 1 images" , Toast.LENGTH_LONG) .show () ;
            }
        }


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
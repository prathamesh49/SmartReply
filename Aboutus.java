package com.guru49.connectit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class Aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.color));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Element adElement = new Element();

        adElement.setTitle( " Designed and Developed by : Trio Solutions India  (+919066605050)") ;
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.onlylogo)



                .setDescription("Trio Solutions India\n\n" +
                        "Technical Experience\n" +
                        "We are well-versed in a variety of business systems, networks, and databases. We work with just about any technology that a small business would encounter. We use this expertise to help customers with small to mid-sized projects." +
                        "\n" + "\n" + "High ROI\n" +
                        "Do you spend most of your IT budget on maintaining your current system? Many companies find that constant maintenance eats into their budget for new technology. By outsourcing your IT management to us, you can focus on what you do best--running your business." +
                        "\n\n" +
                        "Satisfaction Guaranteed\n" +
                        "We are the group of people working on the real time problem facing on the daily basis.To provide effective and optimized solution is our motto\n" +

                        "\n" +
                        "\n" +
                        "Trio Solutions India")


                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adElement)
                .addWebsite ( "triosolutionsindia.com")
                .addGroup("Connect with us")
                .addEmail("triosolutionsindia@gmail.com")
                .addInstagram("trio.solutions.india")
                .addFacebook("Trio Solutions India")


                .create() ;
        setContentView(aboutPage);

    }
}
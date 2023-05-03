package com.bookcan.deliver.productstore.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bookcan.deliver.productstore.R;
import com.bookcan.deliver.productstore.util.localstorage.LocalStorage;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashScreen extends AwesomeSplash {
    LocalStorage localStorage;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
//    }
    @Override
    public void initSplash(ConfigSplash configSplash) {
        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.aqua); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.logo); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Path
//        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
//        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
//        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
//        configSplash.setAnimPathStrokeDrawingDuration(3000);
//        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
//        configSplash.setPathSplashStrokeColor(R.color.colorPrimary); //any color you want form colors.xml
//        configSplash.setAnimPathFillingDuration(3000);
//        configSplash.setPathSplashFillColor(R.color.green_500); //path object filling color

        //Customize Title
        configSplash.setTitleSplash("Loading...");
        configSplash.setTitleTextColor(R.color.colorPrimary);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(500);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        //configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/
    }
    @Override
    public void animationsFinished() {
        localStorage = new LocalStorage(getApplicationContext());
        if (localStorage.isUserLoggedIn()) {
            if (localStorage.getCustomerOrEmployee()==null){
                startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
            }else {
                if (localStorage.getCustomerOrEmployee().equalsIgnoreCase("customer")) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else if (localStorage.getCustomerOrEmployee().equalsIgnoreCase("employee")) {
                    startActivity(new Intent(getApplicationContext(), EmployeeActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                }
            }
        } else {
            startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
    }
}
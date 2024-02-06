package com.argiusGaming.wattscalculator;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private EditText speed;
    private EditText slope;
    private EditText weight;
    private EditText bikeWeight;
    private EditText altitude;
    private EditText wind;

    private EditText rR;
    private EditText cda;
    private EditText loss;
    double ms;
    double fG;
    double fR;
    double fA;
    double POWER;
    double ap;
    double finalVelocity;
    double finalSlope;
    double finalLoss;
    private Button helpBtn;

    public void onCli(View v) {
        if (v.getId() == R.id.helpBtn) {
            Toast.makeText(this, "Crr is usually between 0.002(fastest) to 0.008(slowest)\n CDA is usually between 0.26(fastest) to 0.45(hoods)\n Efficiency is usually between 99%(oiled,new chain) to 95%(dry,old chain) ", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speed = findViewById(R.id.speed);
        slope = findViewById(R.id.slope);
        weight = findViewById(R.id.weight);
        bikeWeight = findViewById(R.id.bikeWeight);
        altitude = findViewById(R.id.altitude);
        wind = findViewById(R.id.wind);
        rR = findViewById(R.id.rR);
        cda = findViewById(R.id.cda);
        loss = findViewById(R.id.loss);
        helpBtn = findViewById(R.id.helpBtn);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        InterstitialAd.load(this, "ca-app-pub-9354629606296819/1302846257", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

        public void onClick (View view){
            TextView wattsOutput = findViewById(R.id.wattsOutput);
            double in = Double.parseDouble(speed.getText().toString());
            double slope1 = Double.parseDouble(slope.getText().toString());
            double weight1 = Double.parseDouble(weight.getText().toString());
            double bikeWeight1 = Double.parseDouble(bikeWeight.getText().toString());
            double rR1 = Double.parseDouble(rR.getText().toString());
            double cda1 = Double.parseDouble(cda.getText().toString());
            double wind1 = Double.parseDouble(wind.getText().toString());
            double loss1 = Double.parseDouble(loss.getText().toString());
            double altitude1 = Double.parseDouble(altitude.getText().toString());
            ap = (1.225 * Math.exp(-0.00011856 * altitude1));
            ms = (in / 3.6);
            finalSlope = slope1 / 100;
            finalLoss = loss1 / 100;
            fG = (9.80665 * Math.sin(Math.atan(finalSlope)) * (weight1 + bikeWeight1));
            fR = (9.80665 * Math.cos(Math.atan(finalSlope)) * (weight1 + bikeWeight1) * rR1);
            fA = (0.5 * cda1 * ap * ((ms + wind1) * (ms + wind1)));
            finalVelocity = (ms / finalLoss);

            POWER = ((fG + fR + fA) * finalVelocity);
            DecimalFormat dcFormat = new DecimalFormat("#.#");

            wattsOutput.setText("WATTS:" + (dcFormat.format(POWER)));
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
        }
    }









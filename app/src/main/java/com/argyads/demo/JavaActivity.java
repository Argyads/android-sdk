package com.argyads.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.argyads.ArgyAds;


public class JavaActivity extends AppCompatActivity {

    // private RewardedAd mRewarded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArgyAds.initialize(this, "");

        // mRewarded = new RewardedAd(this, "ca-app-pub-4537341636281998/1827516515");
        // mRewarded.loadAd();
        setupAdListener();
    }

    void setupAdListener(){
        /* mRewarded.setAdListener(new RewardedAd.AdListener() {

            @Override
            public void onAdImpression() {

            }

            @Override
            public void onAdFailedToShow(@NonNull String s) {
            }

            @Override
            public void onAdDismissed() {
                mRewarded.loadAd();
            }

            @Override
            public void onAdFailedToLoad(@NonNull String s) {
            }

            @Override
            public void onAdLoaded() {
            }

        }); */
    }
}
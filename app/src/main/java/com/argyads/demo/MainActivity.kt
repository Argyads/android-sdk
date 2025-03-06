package com.argyads.demo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.argyads.ArgyAds
import com.argyads.ads.appopen.AppOpenAd
import com.argyads.ads.banner.BannerAd
import com.argyads.ads.interstitial.InterstitialAd
import com.argyads.ads.native.NativeAd
import com.argyads.ads.rewarded.RewardedAd
import com.argyads.ads.rewarded.modules.RewardItem

class MainActivity : AppCompatActivity() {

    private lateinit var appOpenAd: AppOpenAd
    private lateinit var interstitialAd: InterstitialAd
    private lateinit var rewardedAd: RewardedAd
    private lateinit var bannerAd: BannerAd
    private lateinit var nativeAd: NativeAd

    companion object {
        private const val TAG = "TAGD-MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btAppOpen: Button = findViewById(R.id.ad_app_open)
        val btInterstitial: Button = findViewById(R.id.ad_interstitial)
        val btRewarded: Button = findViewById(R.id.ad_rewarded)
        val btBanner: Button = findViewById(R.id.ad_banner)
        val btNative: Button = findViewById(R.id.ad_native)

        // Set up the click listener
        btAppOpen.setOnClickListener { requestAndShowAppOpen() }
        btInterstitial.setOnClickListener { requestAndShowInterstitial() }
        btRewarded.setOnClickListener { requestAndShowRewarded() }
        btBanner.setOnClickListener { requestAndShowBanner() }
        btNative.setOnClickListener { requestAndShowNative() }

        init();
    }

    private fun init() {
        ArgyAds.initialize(this@MainActivity, "")
    }

    private fun requestAndShowAppOpen() {
        appOpenAd = AppOpenAd(this, "/21775744923/example/app-open", false)

        appOpenAd.setAdListener(object : AppOpenAd.AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded successfully")
                appOpenAd.showAd()
            }

            override fun onAdFailedToLoad(errorMessage: String) {
                Log.d(TAG, "Ad failed to load: $errorMessage")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onAdDismissed() {
                Log.d(TAG, "Ad dismissed")
            }

            override fun onAdFailedToShow(errorMessage: String) {
                Log.d(TAG, "Ad failed to show: $errorMessage")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression recorded")
            }

            override fun onAdShowed() {
                Log.d(TAG, "Ad showed")
            }
        })

        appOpenAd.loadAd()
    }

    private fun requestAndShowInterstitial() {
        interstitialAd = InterstitialAd(this, "/21775744923/example/interstitial")

        interstitialAd.setAdListener(object : InterstitialAd.AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded successfully")
                interstitialAd.showAd()
            }

            override fun onAdFailedToLoad(errorMessage: String) {
                Log.d(TAG, "Ad failed to load: $errorMessage")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onAdDismissed() {
                Log.d(TAG, "Ad dismissed")
            }

            override fun onAdFailedToShow(errorMessage: String) {
                Log.d(TAG, "Ad failed to show: $errorMessage")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression recorded")
            }

            override fun onAdShowed() {
                Log.d(TAG, "Ad showed")
            }
        })

        interstitialAd.loadAd()
    }

    private fun requestAndShowRewarded() {
        rewardedAd = RewardedAd(this, "/21775744923/example/rewarded")

        rewardedAd.setAdListener(object : RewardedAd.AdListener() {
            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded successfully")
                rewardedAd.showAd()
            }

            override fun onAdFailedToLoad(errorMessage: String) {
                Log.d(TAG, "Ad failed to load: $errorMessage")
            }

            override fun onAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onAdDismissed() {
                Log.d(TAG, "Ad dismissed")
            }

            override fun onAdFailedToShow(errorMessage: String) {
                Log.d(TAG, "Ad failed to show: $errorMessage")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression recorded")
            }

            override fun onAdShowed() {
                Log.d(TAG, "Ad showed")
            }

            override fun onAdRewardReceived(rewardItem: RewardItem?) {
                super.onAdRewardReceived(rewardItem)
                Log.d(TAG, "Ad Reward Received $rewardItem")
            }
        })

        rewardedAd.loadAd()
    }

    private fun requestAndShowBanner() {
        // Find the container where the ad will be placed
        val adContainer: FrameLayout = findViewById(R.id.adContainer)

        // Initialize and load the ad
        bannerAd = BannerAd(this, "/21775744923/example/adaptive-banner", adContainer)

        bannerAd.setAdListener(object : BannerAd.AdListener() {
            override fun onAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onAdClosed() {
                Log.d(TAG, "Ad closed")
            }

            override fun onAdFailedToLoad(errorMessage: String) {
                Log.d(TAG, "Ad failed to load: $errorMessage")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression")
            }

            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded")
            }

            override fun onAdOpened() {
                Log.d(TAG, "Ad opened")
            }
        })

        bannerAd.loadAd()
    }

    private fun requestAndShowNative() {
        // Find the container where the ad will be placed
        val adContainer: FrameLayout = findViewById(R.id.adContainer)

        // Initialize and load the ad
        nativeAd = NativeAd(this, "/21775744923/example/native", adContainer)

        nativeAd.setAdListener(object : NativeAd.AdListener() {
            override fun onAdClicked() {
                Log.d(TAG, "Ad clicked")
            }

            override fun onAdClosed() {
                Log.d(TAG, "Ad closed")
            }

            override fun onAdFailedToLoad(errorMessage: String) {
                Log.d(TAG, "Ad failed to load: $errorMessage")
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad impression")
            }

            override fun onAdLoaded() {
                Log.d(TAG, "Ad loaded")
            }

            override fun onAdOpened() {
                Log.d(TAG, "Ad opened")
            }
        })

        nativeAd.loadAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        // appOpenAd.destroy()
        interstitialAd.destroy()
        rewardedAd.destroy()
        bannerAd.destroy()
        nativeAd.destroy()
    }

}

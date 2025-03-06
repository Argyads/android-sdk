package com.argyads.ads.interstitial

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback

class InterstitialAd(private val context: Context, private val adUnitId: String) {

    private var mInterstitialAd: AdManagerInterstitialAd? = null
    private var adListener: AdListener? = null
    private var requestId: String? = null


    companion object {
        private const val TAG = "ArgyAds-InterstitialAd"
    }

    fun setAdListener(listener: AdListener) {
        adListener = listener
    }

    fun loadAd() {
        // TODO: Check if ArgyAds initialized correctly else adListener.onFailedToLoad

        // requestId = Logs().requestAd(com.argyads.core.model.AdRequest(adUnitId, "interstitial"))

        Log.d(TAG, "Requested interstitialAd, requestId $requestId")

        requestAdManager()
    }

    private fun requestAdManager() {
        if (mInterstitialAd != null) return

        val adRequest = AdManagerAdRequest.Builder().build()
        AdManagerInterstitialAd.load(context,adUnitId, adRequest, object : AdManagerInterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Log.d(TAG, adError.toString())
                mInterstitialAd = null
                triggerEvent("onAdFailedToLoad", adError.message)
            }

            override fun onAdLoaded(interstitialAd: AdManagerInterstitialAd) {
                // Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
                setupAdCallbacks()
                triggerEvent("onAdLoaded")
            }
        })
    }

    private fun setupAdCallbacks() {

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() = triggerEvent("onAdClicked")

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                mInterstitialAd = null
                triggerEvent("onAdDismissed")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                mInterstitialAd = null
                triggerEvent("onAdFailedToShow", adError.message)
            }

            override fun onAdImpression() = triggerEvent("onAdImpression")

            override fun onAdShowedFullScreenContent() = triggerEvent("onAdShowed")
        }
    }

    fun showAd() {
        if (mInterstitialAd == null) {
            triggerEvent("onAdFailedToShow", "The interstitial ad wasn't ready yet.")
            // Log.d(TAG, "The interstitial ad wasn't ready yet.")
            return
        }

        mInterstitialAd?.show(context as Activity)
    }

    fun isAvailable(): Boolean {
        return mInterstitialAd != null
    }

    fun destroy() {
        mInterstitialAd = null
    }

    private fun triggerEvent(kind: String, message: String = "") {
            // TODO: This control via Cache.config.logs.adCallbacks == true
            /* if (requestId != null) {
                Logs().adCallback(com.argyads.core.model.AdCallback(requestId!!, kind))
            } */

            adListener?.let {
            when (kind) {
                "onAdLoaded" -> it.onAdLoaded()
                "onAdFailedToLoad" -> it.onAdFailedToLoad(message)
                "onAdClicked" -> it.onAdClicked()
                "onAdDismissed" -> it.onAdDismissed()
                "onAdFailedToShow" -> it.onAdFailedToShow(message)
                "onAdImpression" -> it.onAdImpression()
                "onAdShowed" -> it.onAdShowed()
            }
        }
    }

    interface Listener {
        fun onAdLoaded() {}
        fun onAdFailedToLoad(errorMessage: String) {}
        fun onAdClicked() {}
        fun onAdDismissed() {}
        fun onAdFailedToShow(errorMessage: String) {}
        fun onAdImpression() {}
        fun onAdShowed() {}
    }

    open class AdListener : Listener {
        override fun onAdLoaded() {
            // Default empty behavior
        }

        override fun onAdFailedToLoad(errorMessage: String) {
            // Default empty behavior
        }

        override fun onAdClicked() {
            // Default empty behavior
        }

        override fun onAdDismissed() {
            // Default empty behavior
        }

        override fun onAdFailedToShow(errorMessage: String) {
            // Default empty behavior
        }

        override fun onAdImpression() {
            // Default empty behavior
        }

        override fun onAdShowed() {
            // Default empty behavior
        }
    }
}
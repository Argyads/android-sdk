package com.argyads.ads.appopen

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

class AppOpenAd(private val context: Context, private val adUnitId: String, private val refresh: Boolean = false) {

    private var mAppOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    var isShowingAd = false
    private var loadTime: Long = 0

    private var adListener: AdListener? = null
    private var requestId: String? = null

    companion object {
        private const val TAG = "ArgyAds-AppOpenAd"
    }

    fun setAdListener(listener: AdListener) {
        adListener = listener
    }

    fun loadAd() {
        // TODO: Check if ArgyAds initialized correctly else adListener.onFailedToLoad

        // requestId = Logs().requestAd(com.adquimo.core.model.AdRequest(adUnitId, "app_open"))

        Log.d(TAG, "Requested appOpenAd, requestId $requestId")

        requestAdManager()
    }

    private fun requestAdManager() {
        // Do not load ad if there is an unused ad or one is already loading.
        if (isLoadingAd || isAdAvailable()) {
            return
        }

        isLoadingAd = true

        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            context as Activity, adUnitId, adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {

                override fun onAdLoaded(ad: AppOpenAd) {
                    // Called when an app open ad has loaded.
                    // Log.d(TAG, "Ad was loaded.")
                    mAppOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                    triggerEvent("onAdLoaded")
                    setupAdCallbacks()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Called when an app open ad has failed to load.
                    // Log.d(TAG, loadAdError.message)
                    isLoadingAd = false
                    triggerEvent("onAdFailedToLoad")
                }
            })
    }

    private fun setupAdCallbacks() {
        mAppOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdDismissedFullScreenContent() {
                // Called when full screen content is dismissed.
                // Set the reference to null so isAdAvailable() returns false.
                // Log.d(TAG, "Ad dismissed fullscreen content.")
                mAppOpenAd = null
                isShowingAd = false

                // onShowAdCompleteListener.onShowAdComplete()
                if (refresh) {
                    Log.d(TAG, "Refresh is true, request a new app_open")
                    loadAd()
                }
                triggerEvent("onAdDismissed")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when fullscreen content failed to show.
                // Set the reference to null so isAdAvailable() returns false.
                // Log.d(TAG, adError.message)
                mAppOpenAd = null
                isShowingAd = false

                // onShowAdCompleteListener.onShowAdComplete()
                if (refresh) {
                    Log.d(TAG, "Refresh is true, request a new app_open")
                    loadAd()
                }
                triggerEvent("onAdFailedToShow", adError.message)
            }

            override fun onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Log.d(TAG, "Ad showed fullscreen content.")
                triggerEvent("onAdShowed")
            }

            override fun onAdClicked() {
                super.onAdClicked()
                triggerEvent("onAdClicked")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                triggerEvent("onAdImpression")
            }
        }
    }

    fun showAd() {
        if (isShowingAd) {
            Log.d(TAG, "The app open ad is already showing.")
            return
        }

        if (!isAdAvailable()) {
            // onShowAdCompleteListener.onShowAdComplete() // TODO: Check
            Log.d(TAG, "The app open ad is not ready yet.")
            return
        }

        isShowingAd = true
        mAppOpenAd?.show(context as Activity)
    }

    private fun triggerEvent(kind: String, message: String = "") {
            // TODO: This control via Cache.config.logs.adCallbacks == true
            /* if (requestId != null) {
                Logs().adCallback(com.adquimo.core.model.AdCallback(requestId!!, kind))
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

    fun destroy() {
        if(isShowingAd || isLoadingAd) return

        mAppOpenAd = null
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


    /** Utility method to check if ad was loaded more than n hours ago. */
    private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
        val dateDifference: Long = Date().time - loadTime
        val numMilliSecondsPerHour: Long = 3600000
        return dateDifference < numMilliSecondsPerHour * numHours
    }

    /** Check if ad exists and can be shown. */
    private fun isAdAvailable(): Boolean {
        return mAppOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
    }
}
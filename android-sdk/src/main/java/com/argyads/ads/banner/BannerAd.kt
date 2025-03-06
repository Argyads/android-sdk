package com.argyads.ads.banner

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowMetrics
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView

class BannerAd(private val context: Context, private val adUnitId: String,  private val adContainer: ViewGroup) {

    private var adView: AdManagerAdView? = null
    private var adListener: AdListener? = null
    private var requestId: String? = null

    companion object {
        private const val TAG = "ArgyAds-BannerAd"
    }

    fun setAdListener(listener: AdListener) {
        adListener = listener
    }

    fun loadAd() {
        // TODO: Check if ArgyAds initialized correctly else adListener.onFailedToLoad

        // requestId = Logs().requestAd(com.argyads.core.model.AdRequest(adUnitId, "banner"))

        Log.d(TAG, "Requested bannerAd, requestId $requestId")

        requestAdManager()
    }

    private fun requestAdManager() {
        // Start loading the ad in the background.

        // Create a new ad view.
        if (adView == null) {
            adView = AdManagerAdView(context)
            adView!!.adUnitId = adUnitId
            adView!!.setAdSize(adSize)
        }

        adContainer.removeAllViews()
        adContainer.addView(adView)

        val adRequest = AdManagerAdRequest.Builder().build()
        adView!!.loadAd(adRequest)

        setupAdCallbacks()
    }

    private fun setupAdCallbacks() {
        adView?.adListener = object: com.google.android.gms.ads.AdListener() {
            override fun onAdClicked() = triggerEvent("onAdClicked")

            override fun onAdClosed() = triggerEvent("onAdClosed")

            override fun onAdFailedToLoad(adError : LoadAdError) = triggerEvent("onAdFailedToLoad", adError.message)

            override fun onAdImpression() = triggerEvent("onAdImpression")

            override fun onAdLoaded() = triggerEvent("onAdLoaded")

            override fun onAdOpened() = triggerEvent("onAdOpened")
        }
    }

    fun pause() {
        adView?.pause()
    }

    fun resume() {
        adView?.resume()
    }

    fun destroy() {
        adView?.destroy()
    }

    private fun triggerEvent(kind: String, message: String = "") {
            // TODO: This control via Cache.config.logs.adCallbacks == true
            /* if (requestId != null) {
                Logs().adCallback(com.argyads.core.model.AdCallback(requestId!!, kind))
            } */

            adListener?.let {
            when (kind) {
                "onAdClicked" -> it.onAdClicked()
                "onAdClosed" -> it.onAdClosed()
                "onAdFailedToLoad" -> it.onAdFailedToLoad(message)
                "onAdImpression" -> it.onAdImpression()
                "onAdLoaded" -> it.onAdLoaded()
                "onAdOpened" -> it.onAdOpened()
            }
        }
    }

    interface Listener {
        fun onAdClicked() {}
        fun onAdClosed() {}
        fun onAdFailedToLoad(errorMessage: String) {}
        fun onAdImpression() {}
        fun onAdLoaded() {}
        fun onAdOpened() {}
    }

    open class AdListener : Listener {
        override fun onAdClicked() {
            // Code to be executed when the user clicks on an ad.
        }

        override fun onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
        }

        override fun onAdFailedToLoad(errorMessage: String) {
            // Code to be executed when an ad request fails.
        }

        override fun onAdImpression() {
            // Code to be executed when an impression is recorded
            // for an ad.
        }

        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
        }

        override fun onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
        }
    }

    private val adSize: AdSize
        get() {
            val displayMetrics = context.resources.displayMetrics
            val adWidthPixels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowManager = context.getSystemService(WindowManager::class.java)
                val windowMetrics: WindowMetrics = windowManager?.currentWindowMetrics ?: return AdSize.BANNER
                windowMetrics.bounds.width()
            } else {
                displayMetrics.widthPixels
            }
            val density = displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        }
}
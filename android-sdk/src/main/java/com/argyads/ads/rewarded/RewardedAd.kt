package com.argyads.ads.rewarded

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAd(private val context: Context, private val adUnitId: String) {

    private var mRewardedAd: RewardedAd? = null
    private var adListener: AdListener? = null
    private var requestId: String? = null

    companion object {
        private const val TAG = "ArgyAds-RewardedAd"
    }

    fun setAdListener(listener: AdListener) {
        adListener = listener
    }

    fun loadAd() {
        // TODO: Check if ArgyAds initialized correctly else adListener.onFailedToLoad

        // requestId = Logs().requestAd(com.adquimo.core.model.AdRequest(adUnitId, "rewarded"))

        Log.d(TAG, "requested rewarded, requestId $requestId")

        requestAdManager()
    }

    private fun requestAdManager() {
        if (isAvailable()) return

        val adRequest = AdManagerAdRequest.Builder().build()

        RewardedAd.load(context, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mRewardedAd = null
                triggerEvent("onAdFailedToLoad", message = adError.message)
            }

            override fun onAdLoaded(ad: RewardedAd) {
                mRewardedAd = ad
                setupAdCallbacks()
                triggerEvent("onAdLoaded")
            }
        })
    }


    private fun setupAdCallbacks() {
        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                // Log.d(TAG, "Ad was clicked.")
                triggerEvent("onAdClicked")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                // Log.d(TAG, "Ad dismissed fullscreen content.")
                mRewardedAd = null
                triggerEvent("onAdDismissed")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                // Log.e(TAG, "Ad failed to show fullscreen content. $p0")
                mRewardedAd = null
                triggerEvent("onAdFailedToShow", message = p0.message)
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                // Log.d(TAG, "Ad recorded an impression.")
                triggerEvent("onAdImpression")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                // Log.d(TAG, "Ad showed fullscreen content.")
                triggerEvent("onAdShowed")
            }
        }
    }

    fun showAd() {
        if(mRewardedAd == null) {
            // Log.d(TAG, "The interstitial ad wasn't ready yet.")
            triggerEvent("onAdFailedToShow", "The rewarded ad wasn't ready yet.")
            return
        }

        mRewardedAd?.let { ad ->
            ad.show(context as Activity) { rewardItem ->
                triggerEvent("onAdReward", rewardItem = com.argyads.ads.rewarded.modules.RewardItem(rewardItem))
            }
        } ?: run {
            triggerEvent("onAdFailedToShow", "The rewarded ad wasn't ready yet.")
        }
    }

    fun isAvailable(): Boolean {
        return mRewardedAd != null
    }

    fun destroy() {
        mRewardedAd = null
    }

    private fun triggerEvent(kind: String, message: String = "", rewardItem: com.argyads.ads.rewarded.modules.RewardItem? = null) {
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
                "onAdReward" -> it.onAdRewardReceived(rewardItem)
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
        fun onAdRewardReceived(rewardItem: com.argyads.ads.rewarded.modules.RewardItem?) {}
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

        override fun onAdRewardReceived(rewardItem: com.argyads.ads.rewarded.modules.RewardItem?) {
            // Default empty behavior
        }
    }
}
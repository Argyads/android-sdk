package com.argyads.ads.native

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.argyads.R
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView


class NativeAd(private val context: Context, private val adUnitId: String, private val adContainer: ViewGroup) {

    private var nativeAd: NativeAd? = null
    private var adListener: AdListener? = null
    private var requestId: String? = null

    companion object {
        private const val TAG = "ArgyAds-NativeAd"
    }

    fun setAdListener(listener: AdListener) {
        adListener = listener
    }

    fun loadAd() {
        // TODO: Check if ArgyAds initialized correctly else adListener.onFailedToLoad
        Log.d(TAG, "Requested nativeAd, requestId $requestId")
        requestNativeAd()
    }

    private fun requestNativeAd() {
        // Create a new native ad request.
        val adRequest = AdManagerAdRequest.Builder().build()

        // Load the native ad using the ad request.
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad ->
                if ((context as Activity).isDestroyed) {
                    nativeAd?.destroy()
                    return@forNativeAd
                }

                nativeAd = ad
                displayNativeAd()
                setupAdCallbacks()
            }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {
                override fun onAdClicked() = triggerEvent("onAdClicked")
                override fun onAdClosed() = triggerEvent("onAdClosed")
                override fun onAdFailedToLoad(adError: LoadAdError) = triggerEvent("onAdFailedToLoad", adError.message)
                override fun onAdImpression() = triggerEvent("onAdImpression")
                override fun onAdLoaded() = triggerEvent("onAdLoaded")
                override fun onAdOpened() = triggerEvent("onAdOpened")
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build())
            .build()

        adLoader.loadAd(adRequest)
    }

    private fun displayNativeAd() {
        // Check if the ad has loaded and is valid.
        nativeAd?.let {
            adContainer.removeAllViews()

            // Inflate and display native ad view.
            val nativeAdView = LayoutInflater.from(context).inflate(R.layout.gnt_medium_template_view, adContainer, false) as NativeAdView

            // Set the media view (for example, an ImageView or VideoView)
            val mediaView: MediaView = nativeAdView.findViewById(R.id.media_view)

            // Set the headline (TextView)
            val headlineView: TextView = nativeAdView.findViewById(R.id.primary)
            headlineView.text = nativeAd!!.headline

            // Set the icon (ImageView)
            val iconView: ImageView = nativeAdView.findViewById(R.id.icon)
            val icon = nativeAd!!.icon
            if (icon != null) {
                iconView.setImageDrawable(icon.drawable)
                iconView.visibility = View.VISIBLE
            } else {
                iconView.visibility = View.GONE
            }

            // Set the attribution TextView
            // val attributionView: TextView = nativeAdView.findViewById(R.id.native_ad_attribution)
            // attributionView.text = nativeAd!!.attribution // Set the ad's attribution text
            // val attributionTextView: TextView = nativeAdView.findViewById(R.id.native_ad_attribution)


            // Set the attribution text (this can be dynamically set based on the ad's source)
            // attributionTextView.text = String.format("Ad provided by %s", nativeAd!!.advertiser)

            // Set the body (optional)
            val bodyView: TextView = nativeAdView.findViewById(R.id.body)
            bodyView.text = nativeAd!!.body

            // Set the CTA (Call To Action) button
            val ctaButton: AppCompatButton = nativeAdView.findViewById(R.id.cta)
            ctaButton.text = nativeAd!!.callToAction
            ctaButton.setOnClickListener {
                nativeAd!!.callToAction?.let {
                    // Handle the CTA button click action
                }
            }

            // Add the native ad to the NativeAdView
            nativeAdView.setNativeAd(nativeAd!!)

            // Finally, add the nativeAdView to your container
            adContainer.addView(nativeAdView)
        } ?: run {
            triggerEvent("onAdFailedToLoad", "Native ad is null")
        }
    }

    private fun setupAdCallbacks() {
        // Native ads don’t need specific callbacks like banner ads, but you can still handle ad events here if necessary.
    }

    fun destroy() {
        nativeAd?.destroy()
    }

    private fun triggerEvent(kind: String, message: String = "") {
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
            // Code to be executed when the user clicks on a native ad.
        }

        override fun onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on a native ad.
        }

        override fun onAdFailedToLoad(errorMessage: String) {
            // Code to be executed when a native ad request fails.
        }

        override fun onAdImpression() {
            // Code to be executed when an impression is recorded
            // for a native ad.
        }

        override fun onAdLoaded() {
            // Code to be executed when a native ad finishes loading.
        }

        override fun onAdOpened() {
            // Code to be executed when a native ad opens an overlay that
            // covers the screen.
        }
    }
}

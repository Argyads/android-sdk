package com.argyads

import android.content.Context
import android.util.Log
import com.argyads.core.utils.Cache
import com.argyads.core.utils.Library
import com.argyads.core.device.HDevice
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ArgyAds {

    private const val TAG = "ArgyAds"
    private var isInitialized = false
    private var isReady = false
    private val listeners = mutableSetOf<() -> Unit>()

    // CoroutineScope for launching suspend functions
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // Flag to check if ArgyAds SDK is initialized
    @JvmStatic
    val initialized: Boolean
        get() = isInitialized

    // Flag to check if ArgyAds SDK is ready to use
    @JvmStatic
    val ready: Boolean
        get() = isReady

    // Version of the SDK
    @JvmStatic
    val sdkVersion: String
        get() = Library.VERSION

    /**
     * Initialize the SDK.
     */
    @JvmStatic
    @JvmOverloads
    fun initialize(context: Context, appId: String, listener: (() -> Unit)? = null) {
        if (isInitialized) {
            // If already initialized, just return
            Log.d(TAG, "SDK already initialized")
            listener?.invoke()
            return
        }

        // Launch a coroutine to handle the initialization in the background
        coroutineScope.launch(Dispatchers.IO) {
            initializeSuspend(context, appId, listener)
        }
        /*
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@MainActivity) {}
        }
        */
    }

    private suspend fun initializeSuspend(context: Context, appId: String, listener: (() -> Unit)?) {
        // Perform SDK initialization asynchronously
        Log.d(TAG, "Initializing SDK with appId: $appId")

        // Simulate configuration fetching and device registration (this could be network/database operations)
        Cache.initialize(context)
        val deviceId = Cache.configRepository.getDeviceId()

        if (deviceId == null) {
            Log.d(TAG, "DeviceId is null, registering device")
            val device = HDevice(context)
            // register(context, appId, device.metadata())
        } else {
            Cache.token = deviceId
            Log.d(TAG, "DeviceId already set: $deviceId")
        }

        MobileAds.initialize(context) { initializationStatus ->
            initializationStatus.adapterStatusMap.forEach { (adapterClass, status) ->
                if (status.initializationState != AdapterStatus.State.READY) {
                    Log.d(TAG, "Adapter $adapterClass failed to initialize: ${status.description}")
                } else {
                    Log.d(TAG, "Adapter $adapterClass initialized successfully.")
                }
            }

            Log.d(TAG, "Mobile Ads SDK initialized.")
        }

        // Mark the SDK as initialized and ready
        isInitialized = true
        isReady = true

        // Notify listeners that initialization is complete
        listener?.invoke()

        // Trigger log to mark user activity
        // Logs().activity()
    }

    /* private suspend fun register(context: Context, appId: String, device: Device) {
        val response = Cache.restClient.registerDevice(context.packageName, device)
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val jsonString = responseBody.string()
                val jsonObject = JSONObject(jsonString)
                val deviceId = jsonObject.getString("deviceId")
                Log.d(TAG, "Device registered with deviceId: $deviceId")
                Cache.token = String.format("%s_%s", appId, deviceId)
                Cache.configRepository.setDeviceId(String.format("%s_%s", appId, deviceId))
            } ?: Log.d(TAG, "Failed to register device: Response body is null")
        } else {
            Log.d(TAG, "Failed to register device: ${response.code()}")
        }
    } */

    /**
     * Optionally allows external listeners to wait until the SDK is ready.
     */
    @JvmStatic
    fun waitForInitialization(listener: () -> Unit) {
        if (isReady) {
            listener()
        } else {
            listeners.add(listener)
        }
    }

    // Call this method when SDK initialization is complete
    private fun notifyListeners() {
        listeners.forEach { it() }
        listeners.clear()
    }
}
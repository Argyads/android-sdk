package com.argyads.core.repository

import android.content.Context
import android.util.Log
import com.argyads.core.database.dao.ConfigDao


class ConfigRepository(private val context: Context) {

    private val configDao = ConfigDao(context) // Use your custom ConfigDao

    fun setDeviceId(deviceId: String) {
        configDao.insertConfig(deviceId) // Call insertConfig with deviceId as a string
        Log.d(TAG, "DeviceId set to: $deviceId")
    }

    fun getDeviceId(): String? {
        // Check if the config exists
        val config = configDao.getConfig()

        return when {
            config == null -> {
                Log.d(TAG, "Config does not exist.")
                null
            }
            config.deviceId.isNullOrEmpty() -> {
                Log.d(TAG, "deviceId is empty.")
                null
            }
            else -> {
                Log.d(TAG, "Database exists, deviceId is: ${config.deviceId}")
                config.deviceId
            }
        }
    }

    fun dbExists(): Boolean {
        // Check if the database exists using the DAO method
        return configDao.dbExists()
    }

    companion object {
        private const val TAG = "ArgyAds-ConfigRepository"
    }
}
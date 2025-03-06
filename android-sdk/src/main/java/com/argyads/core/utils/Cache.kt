package com.argyads.core.utils

import android.annotation.SuppressLint
import android.content.Context
import com.argyads.core.api.Service
import com.argyads.core.api.Client
import com.argyads.core.repository.ConfigRepository

object Cache {
    @SuppressLint("StaticFieldLeak")
    lateinit var configRepository: ConfigRepository
    lateinit var restClient: Service

    var token: String? = null

    fun initialize(context: Context) {
        configRepository = ConfigRepository(context)
        // restClient = Client.getInstance()
    }
}
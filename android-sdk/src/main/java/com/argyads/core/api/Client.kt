package com.argyads.core.api

import com.argyads.core.utils.Library

object Client {
    private const val BASE_URL = "https://api.argyads.com/sdk/"

    /* fun getInstance(): Service {
        val client = OkHttpClient.Builder()
            .addInterceptor(VersionInterceptor())  // Add the version interceptor
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Service::class.java)
    } */
}

/* class VersionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("aq-version", Library.VERSION)
            .build()
        return chain.proceed(request)
    }
} */
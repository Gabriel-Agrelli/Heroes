package com.example.heroes.data.api

import com.example.heroes.BuildConfig
import com.example.heroes.util.toMd5
import okhttp3.Interceptor
import okhttp3.Response

class InterceptorAPI : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentTimestamp = System.currentTimeMillis()
        val hash =
            "${currentTimestamp}${BuildConfig.marvelPrivateApiKey}${BuildConfig.marvelPublicApiKey}".toMd5()

        val originalRequest = chain.request()
        val httpUrl = originalRequest.url

        val newHttpUrl = httpUrl.newBuilder()
            .addQueryParameter("limit", "20")
            .addQueryParameter("ts", currentTimestamp.toString())
            .addQueryParameter("apikey", BuildConfig.marvelPublicApiKey)
            .addQueryParameter("hash", hash)
            .build()

        val requestBuilder = originalRequest.newBuilder().url(newHttpUrl)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }
}
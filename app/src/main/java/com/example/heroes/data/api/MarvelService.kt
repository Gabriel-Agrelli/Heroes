package com.example.heroes.data.api

import com.example.heroes.data.model.CharacterResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MarvelService {
    private val BASE_URL = "https://gateway.marvel.com/"

    var api: MarvelAPI

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(InterceptorAPI())
            .build()

        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MarvelAPI::class.java)
    }

    fun getCharacters(): Single<CharacterResponse> {
        return api.getCharacters()
    }

    fun getCharactersByName(name: String): Single<CharacterResponse> {
        return api.getCharactersByName(name)
    }
}
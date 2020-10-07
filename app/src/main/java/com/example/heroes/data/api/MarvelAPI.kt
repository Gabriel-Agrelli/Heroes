package com.example.heroes.data.api

import com.example.heroes.data.model.CharacterResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelAPI {
    @GET("v1/public/characters?")
    fun getCharacters(@Query("offset") offset: Int): Single<CharacterResponse>

    @GET("v1/public/characters?")
    fun getCharactersByName(@Query("name") name: String): Single<CharacterResponse>
}

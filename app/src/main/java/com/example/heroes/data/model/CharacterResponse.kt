package com.example.heroes.data.model

data class CharacterResponse(
    val code: Int,
    val status: String,
    val data: CharacterData
)
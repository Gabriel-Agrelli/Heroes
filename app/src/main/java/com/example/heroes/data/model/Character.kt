package com.example.heroes.data.model

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
    val resourceURI: String?
)
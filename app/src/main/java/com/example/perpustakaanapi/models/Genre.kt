package com.example.perpustakaanapi.models

import java.io.Serializable

data class Genres(
    val genres: ArrayList<Genre>
)

data class Genre(
    val id: Int,
    val name: String,
    val tags: Int
): Serializable
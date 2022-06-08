package com.example.perpustakaanapp.models

import java.io.Serializable

data class Categories(
    val categories: ArrayList<Category>
)

data class Category(
    val id: Int,
    val name: String,
    val tags: Int
): Serializable
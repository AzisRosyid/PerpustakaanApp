package com.example.perpustakaanapi.models

import java.io.Serializable

data class Books(
    val books: ArrayList<Book>,
    val totalBooks: Int,
    val totalPages: Int
)

data class BookId(
    val book: Book
)

data class BookLite(
    val author: String,
    val id: String,
    val image: String,
    val title: String
): Serializable

data class Book(
    val author: String,
    val category: Category,
    val dateCreated: String,
    val dateUpdated: String,
    val description: String,
    val genres: List<Genre>,
    val id: String,
    val image: String,
    val download: String,
    val page: Int,
    val publisher: String,
    val title: String,
    val viewCount: Int
): Serializable


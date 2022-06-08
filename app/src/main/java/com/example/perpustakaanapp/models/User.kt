package com.example.perpustakaanapp.models


data class Users(
    val users: ArrayList<Users>
)

data class Profile(
    val user: User
)

data class User(
    val address: String,
    val dateOfBirth: String,
    val email: String,
    val gender: String,
    val id: String,
    val image: String,
    val name: String,
    val password: String?,
    val phoneNumber: String,
    val role: String
)

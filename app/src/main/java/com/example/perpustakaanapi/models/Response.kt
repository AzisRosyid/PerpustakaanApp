package com.example.perpustakaanapi.models

import java.io.Serializable

data class Response(val messages: String): Serializable

data class Token(val token: String): Serializable

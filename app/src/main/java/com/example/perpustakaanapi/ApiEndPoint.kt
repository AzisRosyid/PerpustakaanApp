package com.example.perpustakaanapi

import com.example.perpustakaanapi.models.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint {

    @POST("Auth/Login")
    fun login(@Body body: JsonObject): Call<Token>

    @Multipart
    @POST("Auth/Register")
    fun register(@Part("name") name: RequestBody, @Part("email") email: RequestBody, @Part("password") password: RequestBody, @Part("gender") gender: RequestBody, @Part("role") role: RequestBody, @Part("dateOfBirth") dateOfBirth: RequestBody, @Part("phoneNumber") phoneNumber: RequestBody, @Part("address") address: RequestBody, @Part image: MultipartBody.Part?): Call<Response>

    @GET("Profiles")
    fun getProfile(): Call<Profile>

    @FormUrlEncoded
    @POST("Books/GetBooks")
    fun getBooks(@Field("search") search: String? = null, @Field("content") content: String? = null, @Field("user") user: Int? = null, @Field("page") page: Int? = 1, @Field("pick") pick: Int? = 10, @Field("android") android: Boolean = true): Call<Books>

    @GET("Books/{id}")
    fun getBook(@Path("id") id: String): Call<BookId>

    @GET("Books/DownloadBook/{download}")
    fun getDownloadBook(@Path("download") download: String): Call<ResponseBody>

}
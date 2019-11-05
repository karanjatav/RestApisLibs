package com.example.restapislibs

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceHolderApi {
    @GET("1")
    fun getPosts(): Call<Post>
}
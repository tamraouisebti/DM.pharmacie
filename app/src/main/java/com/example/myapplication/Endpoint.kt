package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Endpoint {
    @GET("getpharmacie")
    fun getpharmacie(): Call<List<pharmacie>>
    @GET("getuser/{numero}/{motdepasse}")
    fun getuser(@Path("numero") numero:String,@Path("motdepasse") motdepasse:String): Call<List<User>>
    @POST("adduser")
    fun adduser (@Body user:User):Call<String>



}
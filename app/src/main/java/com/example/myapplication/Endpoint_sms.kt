package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint_sms {
    @GET("sms/{password}/{tel}")
    fun sendsms(@Path("password") password:String, @Path("tel") tel:String): Call<String>
}
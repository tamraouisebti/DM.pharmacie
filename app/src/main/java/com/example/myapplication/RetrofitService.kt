package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    val endpoint :Endpoint by lazy {
        Retrofit.Builder().baseUrl("http://192.168.43.142:8082/").addConverterFactory(GsonConverterFactory.create()). build().create(Endpoint::class.java)
    }


    val endpoint_sms :Endpoint_sms by lazy {
        Retrofit.Builder().baseUrl("http://192.168.43.142:5000/").addConverterFactory(GsonConverterFactory.create()). build().create(Endpoint_sms::class.java)
    }
}
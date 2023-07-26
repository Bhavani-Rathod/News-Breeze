package com.bhavani.newsbreeze.utils

import com.bhavani.newsbreeze.data.ApiInterface
import com.bhavani.newsbreeze.data.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val newsInstance: ApiInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        newsInstance = retrofit.create(ApiInterface::class.java)
    }
}
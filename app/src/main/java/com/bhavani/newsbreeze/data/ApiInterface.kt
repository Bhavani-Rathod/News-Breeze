package com.bhavani.newsbreeze.data

import com.bhavani.newsbreeze.models.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL="https://newsapi.org/"
const val API_KEY="391b95da0658467d976d422df52b0ac1"

interface ApiInterface {
    @GET("v2/top-headlines?apiKey=$API_KEY")
    fun getHeadlines(@Query("country") country:String,@Query("page") page:Int):Call<News>
}
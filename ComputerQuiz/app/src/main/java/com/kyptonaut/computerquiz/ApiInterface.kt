package com.kyptonaut.computerquiz
//URL: https://opentdb.com/api.php?amount=10&category=18&difficulty=easy&type=multiple
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiInterface {
    @GET
    fun getData(@Url url: String): Call<ResponseData>
}
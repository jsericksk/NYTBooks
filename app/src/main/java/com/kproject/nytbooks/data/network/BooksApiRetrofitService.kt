package com.kproject.nytbooks.data.network

import com.kproject.nytbooks.data.network.models.BookBodyResponse
import com.kproject.nytbooks.utils.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiRetrofitService {

    @GET("lists.json")
    fun getBookList(
        @Query("api-key") apiKey: String = Constants.NYTBooksApi.API_KEY,
        @Query("list") list: String = Constants.NYTBooksApi.LIST_HARDCOVER_FICTION
    ): Call<BookBodyResponse>

    companion object {
        var retrofitService: BooksApiRetrofitService? = null

        fun getInstance() : BooksApiRetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.NYTBooksApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(BooksApiRetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}
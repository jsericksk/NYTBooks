package com.kproject.nytbooks.data.network.models

import com.google.gson.annotations.SerializedName

data class BookResultsResponse(
    @SerializedName("book_details")
    val bookDetailsResponse: List<BookDetailsResponse>
)
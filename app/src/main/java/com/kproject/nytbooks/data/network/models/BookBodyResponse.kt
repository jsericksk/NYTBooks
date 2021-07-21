package com.kproject.nytbooks.data.network.models

import com.google.gson.annotations.SerializedName

data class BookBodyResponse(
    @SerializedName("results")
    val booksResultsResponse: List<BookResultsResponse>
)
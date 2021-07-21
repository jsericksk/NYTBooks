package com.kproject.nytbooks.utils

object Constants {

    object NYTBooksApi {
        const val API_KEY = "SUA_API_KEY"
        const val BASE_URL = "https://api.nytimes.com/svc/books/v3/"
        const val LIST_HARDCOVER_FICTION = "hardcover-fiction"
    }

    object ResultCallback {
        const val SUCCESS = 0
        const val API_ERROR = 1
        const val UNKNOWN_ERROR = 2
    }

}
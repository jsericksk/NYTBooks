package com.kproject.nytbooks.data.repository

import com.kproject.nytbooks.models.Book

sealed class BookResultCallback {
    class Success(val bookList: List<Book>) : BookResultCallback()
    class ApiError(val statusCode: Int) : BookResultCallback()
    object UnknownError : BookResultCallback()
}
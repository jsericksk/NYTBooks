package com.kproject.nytbooks.data.repository

import com.kproject.nytbooks.models.Book

sealed class BookResultCallback {
    object Loading : BookResultCallback()
    data class Success(val bookList: List<Book>) : BookResultCallback()
    data class ApiError(val statusCode: Int) : BookResultCallback()
    object UnknownError : BookResultCallback()
}
package com.kproject.nytbooks.data.repository

interface BookRepository {
    fun getBooks(result: (resultCallback: BookResultCallback) -> Unit)
}
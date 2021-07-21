package com.kproject.nytbooks.iu.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kproject.nytbooks.data.repository.BookRepository
import com.kproject.nytbooks.data.repository.BookResultCallback
import com.kproject.nytbooks.models.Book
import com.kproject.nytbooks.utils.Constants

class MainViewModel(private val bookRepository: BookRepository) : ViewModel() {
    val booksLiveData = MutableLiveData<List<Book>>()
    val resultTypeLiveData = MutableLiveData<Pair<Int, Int?>>()

    fun getBooks() {
        bookRepository.getBooks { bookResult ->
            when (bookResult) {
                is BookResultCallback.Success -> {
                    booksLiveData.postValue(bookResult.bookList)
                    resultTypeLiveData.postValue(Pair(Constants.ResultCallback.SUCCESS, null))
                }
                is BookResultCallback.ApiError -> {
                    if (bookResult.statusCode == 401) {
                        resultTypeLiveData.postValue(
                            Pair(Constants.ResultCallback.API_ERROR, bookResult.statusCode))
                    } else {
                        resultTypeLiveData.postValue(
                            Pair(Constants.ResultCallback.API_ERROR, null))
                    }
                }
                is BookResultCallback.UnknownError -> {
                    resultTypeLiveData.postValue(Pair(Constants.ResultCallback.UNKNOWN_ERROR, 8))
                }
            }
        }
    }

    class MainViewModelFactory constructor(private val repository: BookRepository): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                MainViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel not found")
            }
        }
    }
}
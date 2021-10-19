package com.kproject.nytbooks.iu.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kproject.nytbooks.data.repository.BookRepository
import com.kproject.nytbooks.data.repository.BookResultCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val bookRepository: BookRepository) : ViewModel() {
    private val _bookResultState = MutableStateFlow<BookResultCallback>(BookResultCallback.Loading)
    val bookResultState: StateFlow<BookResultCallback> = _bookResultState

    init {
        viewModelScope.launch {
            bookRepository.getBooks { bookResult ->
                when (bookResult) {
                    is BookResultCallback.Success -> {
                        _bookResultState.value = BookResultCallback.Success(bookResult.bookList)
                    }
                    is BookResultCallback.ApiError -> {
                        if (bookResult.statusCode == 401) {
                            _bookResultState.value = BookResultCallback.ApiError(bookResult.statusCode)
                        } else {
                            _bookResultState.value = BookResultCallback.UnknownError
                        }
                    }
                    is BookResultCallback.UnknownError -> {
                        _bookResultState.value = BookResultCallback.UnknownError
                    }
                    else ->
                        _bookResultState.value = BookResultCallback.Loading
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
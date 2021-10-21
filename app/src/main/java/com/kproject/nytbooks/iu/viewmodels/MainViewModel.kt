package com.kproject.nytbooks.iu.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kproject.nytbooks.data.repository.BookRepository
import com.kproject.nytbooks.data.repository.BookResultCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
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
}
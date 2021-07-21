package com.kproject.nytbooks.data.network

import com.kproject.nytbooks.data.network.models.BookBodyResponse
import com.kproject.nytbooks.data.repository.BookRepository
import com.kproject.nytbooks.data.repository.BookResultCallback
import com.kproject.nytbooks.models.Book
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Classe responsável pela chamada à API de livros e pelo tratamento da resposta
 * dos dados obtidos.
 */
class BooksApiDataSource : BookRepository {

    override fun getBooks(result: (resultCallback: BookResultCallback) -> Unit) {
        BooksApiRetrofitService.getInstance().getBookList().enqueue(object: Callback<BookBodyResponse> {
            override fun onResponse(call: Call<BookBodyResponse>, response: Response<BookBodyResponse>) {
                when {
                    response.isSuccessful -> {
                        val bookList: MutableList<Book> = mutableListOf()
                        response.body()?.let { bookBodyJSON ->
                            for (bookResult in bookBodyJSON.booksResultsResponse) {
                                /**
                                 * Nota: Apesar de conter apenas um objeto, book_details
                                 * da API é um array, por isso a necessidade de obter
                                 * o primeiro (e único) elemento de BookDetailsResponse.
                                 */
                                val book = Book(
                                    title = bookResult.bookDetailsResponse.first().title,
                                    author = bookResult.bookDetailsResponse.first().author,
                                    description = bookResult.bookDetailsResponse.first().description
                                )
                                bookList.add(book)
                            }
                        }

                        result(BookResultCallback.Success(bookList))
                    }
                    else -> result(BookResultCallback.ApiError(response.code()))
                }
            }

            override fun onFailure(call: Call<BookBodyResponse>, t: Throwable) {
                result(BookResultCallback.UnknownError)
            }
        })
    }
}
package com.kproject.nytbooks.iu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kproject.nytbooks.databinding.RecyclerviewItemBooksBinding
import com.kproject.nytbooks.models.Book

class MainAdapter(
    private val bookList: List<Book>,
    private val onItemClickListener: ((book: Book) -> Unit)
) : RecyclerView.Adapter<MainAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, view: Int): BookViewHolder {
        val binding = RecyclerviewItemBooksBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun getItemCount() = bookList.size

    override fun onBindViewHolder(viewHolder: BookViewHolder, position: Int) {
        viewHolder.bindView(bookList[position])
    }

    inner class BookViewHolder(
       private val binding: RecyclerviewItemBooksBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(book: Book) {
            with(binding) {
                title.text = book.title
                author.text = book.author
            }

            itemView.setOnClickListener {
                onItemClickListener(book)
            }
        }
    }
}

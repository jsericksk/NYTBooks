package com.kproject.nytbooks.iu.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kproject.nytbooks.R
import com.kproject.nytbooks.data.repository.BookResultCallback
import com.kproject.nytbooks.databinding.ActivityMainBinding
import com.kproject.nytbooks.iu.adapters.MainAdapter
import com.kproject.nytbooks.iu.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = binding.includedMainToolbar.mainToolbar
        setSupportActionBar(toolbar)

        lifecycleScope.launchWhenCreated {
            mainViewModel.bookResultState.collect { bookResult ->
                when (bookResult) {
                    is BookResultCallback.Success -> {
                        with(binding.rvBookList) {
                            layoutManager = LinearLayoutManager(
                                this@MainActivity,
                                LinearLayoutManager.VERTICAL, false
                            )
                            adapter = MainAdapter(bookResult.bookList) {
                                val intent =
                                        BookDetailsActivity.getStartIntent(
                                            this@MainActivity,
                                            it.title,
                                            it.author,
                                            it.description
                                        )
                                startActivity(intent)
                            }
                        }
                        binding.vpLayoutType.displayedChild = VIEW_FLIPPER_RECYCLERVIEW
                    }
                    is BookResultCallback.ApiError -> {
                        binding.vpLayoutType.displayedChild = VIEW_FLIPPER_TEXTVIEW_ERROR
                        binding.tvError.text = getString(R.string.api_error_401)
                    }
                    is BookResultCallback.UnknownError -> {
                        binding.vpLayoutType.displayedChild = VIEW_FLIPPER_TEXTVIEW_ERROR
                        binding.tvError.text = getString(R.string.api_unknown_error)
                    }
                }
            }
        }
    }

    /**
     * Constantes que representam a posi????o do componente no ViewFlipper.
     * A posi????o inicial (0) cont??m o ProgressBar.
     */
    companion object {
        private const val VIEW_FLIPPER_RECYCLERVIEW = 1
        private const val VIEW_FLIPPER_TEXTVIEW_ERROR = 2
    }
}
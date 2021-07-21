package com.kproject.nytbooks.iu.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kproject.nytbooks.iu.adapters.MainAdapter
import com.kproject.nytbooks.R
import com.kproject.nytbooks.data.network.BooksApiDataSource
import com.kproject.nytbooks.databinding.ActivityMainBinding
import com.kproject.nytbooks.iu.viewmodels.MainViewModel
import com.kproject.nytbooks.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = binding.includedMainToolbar.mainToolbar
        setSupportActionBar(toolbar)

        mainViewModel = ViewModelProvider(this,
            MainViewModel.MainViewModelFactory(BooksApiDataSource())).get(MainViewModel::class.java)

        mainViewModel.booksLiveData.observe(this, {
            it?.let {
                with(binding.rvBookList) {
                    layoutManager =
                            LinearLayoutManager(this@MainActivity,
                                LinearLayoutManager.VERTICAL, false)

                    adapter = MainAdapter(it) {
                        val intent =
                                BookDetailsActivity.getStartIntent(this@MainActivity, it.title,
                                                                   it.author, it.description)
                        startActivity(intent)
                    }
                }
            }
        })

        mainViewModel.resultTypeLiveData.observe(this, {
            it?.let { resultType ->
                /**
                 * resultType.first(): tipo de resultado; um valor de Constants.ResultCallback
                 * resultType.second(): statusCode da resposta da API (pode ser null)
                 */
                when (resultType.first) {
                    Constants.ResultCallback.SUCCESS -> {
                        binding.vpLayoutType.displayedChild = VIEW_FLIPPER_RECYCLERVIEW
                    }
                    Constants.ResultCallback.API_ERROR -> {
                        binding.vpLayoutType.displayedChild = VIEW_FLIPPER_TEXTVIEW_ERROR
                        binding.tvError.text = getString(R.string.api_error_401)
                    }
                    Constants.ResultCallback.UNKNOWN_ERROR -> {
                        binding.vpLayoutType.displayedChild = VIEW_FLIPPER_TEXTVIEW_ERROR
                        binding.tvError.text = getString(R.string.api_unknown_error)
                    }
                }
            }
        })

        mainViewModel.getBooks()
    }

    companion object {
        // Constantes que representam a posição do componente no ViewFlipper
        private const val VIEW_FLIPPER_RECYCLERVIEW = 1
        private const val VIEW_FLIPPER_TEXTVIEW_ERROR = 2
    }
}
package com.kproject.nytbooks.di

import com.kproject.nytbooks.data.network.BooksApiDataSource
import com.kproject.nytbooks.data.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideBookRepository(): BookRepository {
        return BooksApiDataSource()
    }
}
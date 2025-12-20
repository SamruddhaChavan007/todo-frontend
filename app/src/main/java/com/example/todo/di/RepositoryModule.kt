package com.example.todo.di

import com.example.todo.core.TokenStore
import com.example.todo.data.remote.TodoApi
import com.example.todo.data.repo.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: TodoApi,
        tokenStore: TokenStore
    ): AuthRepository = AuthRepository(api, tokenStore)
}

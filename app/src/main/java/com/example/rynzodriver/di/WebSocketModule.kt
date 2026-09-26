package com.example.rynzodriver.di

import com.example.rynzodriver.data.repository.websocket.WebSocketRepositoryImpl
import com.example.rynzodriver.domain.repository.websocket.WebSocketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebSocketModule {

    @Binds
    @Singleton
    abstract fun bindWebSocketRepository(
        impl: WebSocketRepositoryImpl
    ): WebSocketRepository
}

package com.example.rynzodriver.di

import com.example.rynzodriver.data.repository.auth.AuthRepositoryImpl
import com.example.rynzodriver.data.repository.trips.TripRepositoryImpl
import com.example.rynzodriver.domain.repository.auth.AuthRepository
import com.example.rynzodriver.domain.repository.trips.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTripRepository(
        tripRepositoryImpl: TripRepositoryImpl
    ): TripRepository
}

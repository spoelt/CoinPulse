package com.spoelt.coinpulse.di

import com.spoelt.coinpulse.data.repository.TickerRepositoryImpl
import com.spoelt.coinpulse.domain.repository.TickerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * This module is responsible for defining the creation of any repository dependencies used in the
 * application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTickerRepository(
        impl: TickerRepositoryImpl
    ): TickerRepository
}
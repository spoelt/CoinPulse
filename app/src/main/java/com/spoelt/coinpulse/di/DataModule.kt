package com.spoelt.coinpulse.di

import android.content.Context
import androidx.room.Room
import com.spoelt.coinpulse.data.connectivity.ConnectivityObserverImpl
import com.spoelt.coinpulse.data.local.dao.TickerDao
import com.spoelt.coinpulse.data.local.db.TickerDatabase
import com.spoelt.coinpulse.data.remote.api.CoinService
import com.spoelt.coinpulse.data.remote.api.CoinServiceImpl
import com.spoelt.coinpulse.domain.connectivity.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.gson.gson
import javax.inject.Singleton

/**
 * This module is responsible for defining the creation of any dependencies used in the
 * data module, e.g. http client, dao, database.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideCoinService(
        client: HttpClient
    ): CoinService = CoinServiceImpl(client)

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            gson {
                serializeSpecialFloatingPointValues()
                setLenient()
            }
        }
    }

    @Provides
    @Singleton
    fun provideTickerDatabase(
        @ApplicationContext applicationContext: Context,
    ): TickerDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TickerDatabase::class.java,
            "ticker-database.db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideTickerDAO(
        database: TickerDatabase,
    ): TickerDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext applicationContext: Context): ConnectivityObserver {
        return ConnectivityObserverImpl(applicationContext)
    }
}
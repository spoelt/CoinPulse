package com.spoelt.coinpulse.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spoelt.coinpulse.data.local.dao.TickerDao
import com.spoelt.coinpulse.data.local.model.TickerEntity

@Database(
    entities = [TickerEntity::class],
    version = 1
)
abstract class TickerDatabase : RoomDatabase() {
    abstract val dao: TickerDao
}
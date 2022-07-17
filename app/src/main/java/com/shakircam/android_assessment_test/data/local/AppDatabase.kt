package com.shakircam.android_assessment_test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shakircam.android_assessment_test.model.Repository.Item


@Database(entities = [Item::class],version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repositoryDao() : RepositoryDao
}
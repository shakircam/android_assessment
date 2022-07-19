package com.shakircam.android_assessment_test.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shakircam.android_assessment_test.model.Repository.Item


@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListOfRepository(item: List<Item>)


    @Query("SELECT * FROM repository_table ")
    fun getAllRepositoryItem(): LiveData<List<Item>>



}
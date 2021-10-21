package com.example.testlauncher2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AppDatabaseDao {
    @Insert
    suspend fun insert(app: FavoriteApp)

    @Update
    suspend fun update(app: FavoriteApp)

    @Query("SELECT * FROM favorite_app ORDER BY appId DESC")
    fun getAllApps(): LiveData<List<FavoriteApp>>

    @Query("DELETE FROM favorite_app WHERE appId = :key")
    suspend fun deleteApp(key: Long)
}
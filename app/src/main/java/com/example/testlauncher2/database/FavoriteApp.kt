package com.example.testlauncher2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_app")
class FavoriteApp (
    @PrimaryKey(autoGenerate = true)
    var appId: Long = 0L,

    @ColumnInfo(name = "package_app")
    var packageApp: String = ""
    )

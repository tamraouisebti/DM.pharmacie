package com.example.myapplication

import androidx.room.Dao
import androidx.room.Query


@Dao
interface pharmacieDao {
    @Query("Select * From pharmacies")
    fun getpharmacie():List<pharmacie>
}
package com.example.apotyk.obat

import androidx.room.*

@Dao
interface ObatDao {
    @Insert
    suspend fun addObat(note: Obat)
    @Update
    suspend fun updateObat(note: Obat)
    @Delete
    suspend fun deleteObat(note: Obat)
    @Query("SELECT * FROM obat")
    suspend fun getObats() : List<Obat>
    @Query("SELECT * FROM obat WHERE idObat =:obat_id")
    suspend fun getObat(obat_id: Int) : List<Obat>
}
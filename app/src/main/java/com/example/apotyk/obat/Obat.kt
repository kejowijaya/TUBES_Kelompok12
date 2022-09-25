package com.example.apotyk.obat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Obat (
    @PrimaryKey(autoGenerate = true)
    val idObat: Int,
    val namaObat: String,
    val jumlahObat: String//
)
package com.example.apotyk.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val tanggalLahir: String,
    val email: String,
    val nomorTelepon: String,
    val password: String//
)
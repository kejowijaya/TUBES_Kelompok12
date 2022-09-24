package com.example.login.user

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(note: User)
    @Update
    suspend fun updateUser(note: User)
    @Delete
    suspend fun deleteUser(note: User)
    @Query("SELECT * FROM user")
    suspend fun getUsers() : List<User>
    @Query("SELECT * FROM user WHERE id =:username_id")
    suspend fun getUser(username_id: Int) : List<User>
}
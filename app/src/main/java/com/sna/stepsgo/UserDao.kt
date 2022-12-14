package com.sna.stepsgo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {



        @Insert
        suspend fun  addUser(user: User)



        @Query("SELECT * FROM user ORDER BY id DESC")
        suspend fun getAllUser(): List<User>



        @Update
        suspend fun updateuUer(user: User)

        @Delete
        suspend fun deleteUser(user: User)



        @Query("DELETE FROM user")
        suspend fun deleteAll()







}
package com.project.petmedicalmap.roomDB.pharmacy

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PharmacyDao {
    @Query("SELECT COUNT(*) FROM Pharmacy")
    suspend fun getCount(): Int

    @Query("SELECT * FROM Pharmacy")
    suspend fun getAllPharmacyData(): List<PharmacyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPharmacies(pharmacies: List<PharmacyEntity>)

}
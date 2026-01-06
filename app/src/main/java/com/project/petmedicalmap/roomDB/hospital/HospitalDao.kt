package com.project.petmedicalmap.roomDB.hospital

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HospitalDao {
    @Query("SELECT COUNT(*) FROM hospitals")
    suspend fun getCount(): Int

    @Query("SELECT * FROM hospitals")
    suspend fun getAllHospitalData(): List<HospitalEntity>

    @Query("SELECT * FROM hospitals WHERE name LIKE '%24%'")
    suspend fun getDataLike24(): List<HospitalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHospitals(hospitals: List<HospitalEntity>)

}
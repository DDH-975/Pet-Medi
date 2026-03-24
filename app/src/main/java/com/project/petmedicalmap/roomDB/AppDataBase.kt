package com.project.petmedicalmap.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.petmedicalmap.roomDB.hospital.HospitalDao
import com.project.petmedicalmap.roomDB.hospital.HospitalEntity
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyDao
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyEntity

@Database(entities = [HospitalEntity::class, PharmacyEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun hosDao(): HospitalDao
    abstract fun pharDao(): PharmacyDao

}
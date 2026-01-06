package com.project.petmedicalmap.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.petmedicalmap.roomDB.hospital.HospitalDao
import com.project.petmedicalmap.roomDB.hospital.HospitalEntity
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyDao
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyEntity

@Database(entities = [HospitalEntity::class, PharmacyEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun hosDao(): HospitalDao
    abstract fun pharDao(): PharmacyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_db"
                ).build().also { INSTANCE = it }
            }
    }

}
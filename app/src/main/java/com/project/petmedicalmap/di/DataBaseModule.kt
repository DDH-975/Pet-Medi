package com.project.petmedicalmap.di

import android.content.Context
import androidx.room.Room
import com.project.petmedicalmap.roomDB.AppDataBase
import com.project.petmedicalmap.roomDB.hospital.HospitalDao
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun proviedsDataBase(context: Context): AppDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "app_db"
        ).build()
    }

    @Provides
    fun providesHosDao(database: AppDataBase): HospitalDao{
        return database.hosDao()
    }

    @Provides
    fun proivedsPharDao(database: AppDataBase): PharmacyDao{
        return  database.pharDao()
    }
}


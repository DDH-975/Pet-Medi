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
    fun providesDataBase(context: Context): AppDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "app_db"
        ).build()
    }

    @Provides
    fun proidesHosDao(dataBase: AppDataBase): HospitalDao {
        return dataBase.hosDao()
    }

    @Provides
    fun providesPharDao(dataBase: AppDataBase): PharmacyDao {
        return dataBase.pharDao()
    }

}
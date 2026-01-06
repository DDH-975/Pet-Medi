package com.project.petmedicalmap

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.project.petmedicalmap.roomDB.AppDataBase
import com.project.petmedicalmap.roomDB.hospital.HospitalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HospitalViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDataBase.getInstance(application)
    private val repository = HospitalRepository(application, db.hosDao())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData() }
    }




}
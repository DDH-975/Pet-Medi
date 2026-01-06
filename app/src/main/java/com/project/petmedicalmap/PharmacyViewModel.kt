package com.project.petmedicalmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.petmedicalmap.roomDB.AppDataBase
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PharmacyViewModel(application: Application) : AndroidViewModel(application) {
    val db = AppDataBase.getInstance(application)
    val repo = PharmacyRepository(application,  db.pharDao())


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertData()
        }
    }

}
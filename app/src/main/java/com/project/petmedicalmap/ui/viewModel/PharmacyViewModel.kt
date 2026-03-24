package com.project.petmedicalmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.petmedicalmap.roomDB.AppDataBase
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyEntity
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PharmacyViewModel(application: Application) : AndroidViewModel(application) {
    val db = AppDataBase.getInstance(application)
    val repo = PharmacyRepository(application, db.pharDao())

    val pharmacyData = MutableLiveData<List<PharmacyEntity>>()
    val _pharmacyData: LiveData<List<PharmacyEntity>> = pharmacyData


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertData()
        }
    }

    fun getPharmacyData() {
        viewModelScope.launch(Dispatchers.IO) {
            val pharData = repo.getParmacyData()
            pharmacyData.postValue(pharData)
        }
    }

}
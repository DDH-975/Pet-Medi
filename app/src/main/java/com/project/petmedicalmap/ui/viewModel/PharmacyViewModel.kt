package com.project.petmedicalmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.petmedicalmap.roomDB.AppDataBase
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyEntity
import com.project.petmedicalmap.roomDB.pharmacy.PharmacyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class PharmacyViewModel(
    application: Application,
    private val repo: PharmacyRepository
    ) : AndroidViewModel(application) {

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
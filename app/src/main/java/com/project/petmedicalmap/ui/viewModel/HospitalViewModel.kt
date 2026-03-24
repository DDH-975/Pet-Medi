package com.project.petmedicalmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.petmedicalmap.roomDB.hospital.HospitalEntity
import com.project.petmedicalmap.roomDB.hospital.HospitalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val application: Application,
    private val repository: HospitalRepository
) : AndroidViewModel(application) {


    private val hospitalData = MutableLiveData<List<HospitalEntity>>()
    val _hospitalData: LiveData<List<HospitalEntity>> = hospitalData

    private val hospiatal24thData = MutableLiveData<List<HospitalEntity>>()
    val _Hospiatal24thData: LiveData<List<HospitalEntity>> = hospiatal24thData


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData()
        }
    }

    fun getHosAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            val hosData = repository.getHospitalData()
            hospitalData.postValue(hosData)
        }
    }

    fun get24thHosData() {
        viewModelScope.launch(Dispatchers.IO) {
            val hos24thData = repository.get24thHosData()
            hospiatal24thData.postValue(hos24thData)
        }
    }
}
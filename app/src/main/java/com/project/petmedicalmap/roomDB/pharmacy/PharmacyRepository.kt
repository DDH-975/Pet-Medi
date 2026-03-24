package com.project.petmedicalmap.roomDB.pharmacy

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.petmedicalmap.roomDB.JsonReader
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PharmacyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dao: PharmacyDao
) {

    suspend fun insertData() {
        if (dao.getCount() > 0) return

        val gson = Gson()

        val pharJsonStr = JsonReader.readJson(context, fileName = "pharmacy.json")

        val pharmacyMap: PharmacyMapDto =
            gson.fromJson(pharJsonStr, object : TypeToken<PharmacyMapDto>() {}.type)

        val pharmacyEntities = pharmacyMap.map { (pharmacyName, pharmacyDto) ->
            PharmacyEntity(
                name = pharmacyName,
                oprTimeInfo = pharmacyDto.OPR_TIME_INFO,
                tel = pharmacyDto.RPRS_TELNO,
                address = pharmacyDto.RN_ADDR,
                homePage = pharmacyDto.HMPG_URL,
                lat = pharmacyDto.LA_VLUE,
                lng = pharmacyDto.LO_VLUE
            )
        }

        dao.insertAllPharmacies(pharmacyEntities)
    }


    suspend fun getParmacyData(): List<PharmacyEntity> {
        val pharmacyAllData = dao.getAllPharmacyData()
        return pharmacyAllData
    }


}
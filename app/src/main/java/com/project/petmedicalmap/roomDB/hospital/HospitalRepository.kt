package com.project.petmedicalmap.roomDB.hospital

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.petmedicalmap.roomDB.JsonReader

class HospitalRepository(
    private val context: Context,
    private val dao: HospitalDao
) {

    suspend fun insertData() {
        val countBefore = dao.getCount()
        Log.d("db_test", "before insert count = $countBefore")

        if (dao.getCount() > 0) return

        val gson = Gson()

        // JSON 파일을 읽어와서 String으로 변환
        val hosjsonString = JsonReader.readJson(context, fileName = "hospital.json")

        // String 형태의 JSON 데이터를 GSON을 사용하여 Map 구조의 DTO 객체로 변환
        val hospitalMap: HospitalMapDto =
            gson.fromJson(hosjsonString, object : TypeToken<HospitalMapDto>() {}.type)

        // Map 형태의 데이터를 DB 저장 및 관리를 위한 Entity 리스트로 변환
        val hospitalEntities = hospitalMap.map { (hospitalName, hospitalDto) ->
            HospitalEntity(
                name = hospitalName,
                addres = hospitalDto.RN_ADDR,
                oprTimeInfo = hospitalDto.OPR_TIME_INFO,
                tel = hospitalDto.RPRS_TELNO,
                homepage = hospitalDto.HMPG_URL,
                lat = hospitalDto.LA_VLUE,
                lng = hospitalDto.LO_VLUE
            )
        }
        dao.insertAllHospitals(hospitalEntities)

        val countAfter = dao.getCount()
        Log.d("db_test", "after insert count = $countAfter")
    }

    suspend fun getHospitalData(): List<HospitalEntity> {
        val hospitalData = dao.getAllHospitalData()
        return hospitalData
    }

    suspend fun get24thHosData(): List<HospitalEntity> {
        val Hos24thData = dao.getDataLike24()
        return Hos24thData
    }

}

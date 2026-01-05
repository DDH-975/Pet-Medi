package com.project.petmedicalmap.roomDB.hospital

data class HospitalDto(
    val RN_ADDR: String?,
    val OPR_TIME_INFO: String?,
    val RPRS_TELNO: String?,
    val HMPG_URL: String?,
    val LA_VLUE: Double?,
    val LO_VLUE: Double?
)

typealias HospitalMapDto = Map<String, HospitalDto>
package com.project.petmedicalmap.roomDB.hospital

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "hospitals")
data class HospitalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    @SerializedName("RN_ADDR")
    val addres: String?,

    @SerializedName("OPR_TIME_INFO")
    val oprTimeInfo: String?,

    @SerializedName("RPRS_TELNO")
    val tel: String?,

    @SerializedName("HMPG_URL")
    val homepage: String?,

    @SerializedName("LA_VLUE")
    val lat: Double?,

    @SerializedName("LO_VLUE")
    val lng: Double?
)
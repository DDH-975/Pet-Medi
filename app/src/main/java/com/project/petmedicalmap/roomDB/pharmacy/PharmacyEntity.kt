package com.project.petmedicalmap.roomDB.pharmacy

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Pharmacy")
data class PharmacyEntity(
    @PrimaryKey
    val name:String,

    @SerializedName("RN_ADDR")
    val address: String,

    @SerializedName("OPR_TIME_INFO")
    val oprTimeInfo: String,

    @SerializedName("RPRS_TELNO")
    val tel: String,

    @SerializedName("HMPG_URL")
    val homePage: String,

    @SerializedName("LA_VLUE")
    val lat: Double,

    @SerializedName("LO_VLUE")
    val lng: Double
)
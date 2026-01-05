package com.project.petmedicalmap.roomDB

import android.content.Context

object JsonReader {
    fun readJson(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }

    }
}
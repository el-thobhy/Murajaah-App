package com.elthobhy.murajaah.repository

import android.content.Context
import android.util.Log
import com.elthobhy.murajaah.models.Album
import com.elthobhy.murajaah.models.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object Repository {
    fun getFataTopChartFromAssets(context: Context?): List<Song>?{
        val json: String?
        return try {
            val inputStream = context?.assets?.open("json/topcharts.json")
            json = inputStream?.bufferedReader().use { it?.readText() }
            Log.d("repo", "getFataTopChartFromAssets: $json")
            val groupListType = object : TypeToken<List<Song?>>() {}.type
            Gson().fromJson(json, groupListType)
        }catch (e: IOException){
            e.printStackTrace()
            Log.e("repo", "getFataTopChartFromAssets: ${e.message}" )
            null
        }
    }
    fun getFataTopAlbumFromAssets(context: Context?): List<Album>?{
        val json: String?
        return try {
            val inputStream = context?.assets?.open("json/topalbums.json")
            json = inputStream?.bufferedReader().use { it?.readText() }
            Log.d("repo", "getFataTopChartFromAssets: $json")
            val groupListType = object : TypeToken<List<Album?>>() {}.type
            Gson().fromJson(json, groupListType)
        }catch (e: IOException){
            e.printStackTrace()
            Log.e("repo", "getFataTopChartFromAssets: ${e.message}" )
            null
        }
    }
}
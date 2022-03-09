package com.elthobhy.murajaah.repository

import android.content.Context
import android.util.Log
import com.elthobhy.murajaah.models.Album
import com.elthobhy.murajaah.models.Song
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
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
    fun addDataToTopCharts(){
        val databaseTopCharts = FirebaseDatabase.getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("top_charts")
        val songs = mutableListOf<Song?>()

        //mengambil data dari firebase storage
        FirebaseStorage
            .getInstance("gs://murajaah-f040b.appspot.com")
            .reference
            .child("musics")
            .listAll()
            .addOnSuccessListener { listResult->
                listResult.items.forEach{ item->
                    item.downloadUrl
                        .addOnSuccessListener { uri->
                            val names = item.name.split("_")

                            val artistName = names[0].trim()
                            val albumName = names[1].trim()
                            val songName = names[2].trim()
                            val yearAlbum = names[3].trim().replace(".mp3","")

                            val keySong = databaseTopCharts.push().key
                            songs.add(Song(
                                keySong = keySong,
                                nameSong = songName,
                                uriSong = uri.toString(),
                                artistSong = artistName,
                                yearSong = yearAlbum.toInt(),
                                albumNameSong = albumName
                            ))
                            databaseTopCharts.setValue(songs)

                        }
                        .addOnFailureListener { e->
                            Log.e("repo", "addDataToTopCharts: download uri: ${e.message}", )
                        }
                }
            }
            .addOnFailureListener { e->
                Log.e("repo", "addDataToTopCharts: ${e.printStackTrace()}", )
                Log.e("repo", "addDataToTopCharts: ${e.message}", )
            }
    }
}
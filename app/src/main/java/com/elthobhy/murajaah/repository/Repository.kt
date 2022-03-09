package com.elthobhy.murajaah.repository

import android.content.Context
import android.util.Log
import com.elthobhy.murajaah.models.Album
import com.elthobhy.murajaah.models.Song
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        val databaseTopCharts = FirebaseDatabase
            .getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("top_charts")
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
    fun addDataImageToTopCharts(){
        val databaseTopCharts = FirebaseDatabase.getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("top_charts")
        //mengambil data dari Database Storage
        FirebaseStorage
            .getInstance("gs://murajaah-f040b.appspot.com")
            .reference
            .child("images")
            .listAll()
            .addOnSuccessListener{ listResult->
                listResult.items.forEach {item->
                    item.downloadUrl
                        .addOnSuccessListener {uri->
                            val albumName = item.name.replace(".png","").trim()
                            databaseTopCharts.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (snap in snapshot.children){
                                        val song = snap.getValue(Song::class.java)
                                        if(song?.albumNameSong == albumName){
                                            databaseTopCharts.child(snap.key.toString())
                                                .updateChildren(mapOf(
                                                    "image_song" to uri.toString()
                                                ))
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Log.e("erro", "onCancelled: ${error.message}", )
                                }

                            })
                        }
                        .addOnFailureListener {e->
                            Log.e("repo", "addDataImageToTopCharts: download ${e.message}", )
                        }
                }
            }
            .addOnFailureListener {e->
                Log.e("repo", "addDataImageToTopCharts: ${e.message}", )
            }
    }
    fun addDataToTopAlbum(){
        val databaseTopAlbum  = FirebaseDatabase
            .getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("top_albums")
        val databaseTopCharts = FirebaseDatabase
            .getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("top_charts")

        val albums = mutableListOf<Album>()
        var topCharts = mutableListOf<Song>()

        databaseTopCharts.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.toList()
                val data = items.sortedWith(compareBy {
                    it.getValue(Song::class.java)?.albumNameSong
                })

                for(i in data.indices){
                    var nextSong : Song? = Song()
                    if(i<data.size -1){
                        nextSong = data[i+1].getValue(Song::class.java)
                    }
                    val song = data[i].getValue(Song::class.java)
                    if(song != null ){
                        topCharts.add(song)
                    }

                    if(song?.albumNameSong != nextSong?.albumNameSong){
                        val keyAlbum = databaseTopAlbum.push().key
                        albums.add(
                            Album(
                            artistAlbum = song?.artistSong,
                            keyAlbum = keyAlbum,
                            nameAlbum = song?.albumNameSong,
                            yearAlbum = song?.yearSong,
                            songs = topCharts
                        )
                        )
                        topCharts = mutableListOf()
                    }
                }
                databaseTopAlbum.setValue(albums)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("repo", "onCancelled: ${error.message}", )
            }

        })
    }
}
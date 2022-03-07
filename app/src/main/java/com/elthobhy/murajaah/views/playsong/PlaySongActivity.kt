package com.elthobhy.murajaah.views.playsong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityPlaySongBinding
import com.elthobhy.murajaah.models.Song

class PlaySongActivity : AppCompatActivity() {

    private var _binding: ActivityPlaySongBinding? = null
    private val binding get() = _binding
    private var position = 0
    private var songs: MutableList<Song>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlaySongBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        getData()
        onClick()
    }

    private fun getData() {
        //getDAta from Intent
        if(intent!=null){
            songs = intent.getParcelableArrayListExtra(KEY_SONGS)
            position = intent.getIntExtra(KEY_POSITION, 0)
            songs?.let {
                val song = it[position]
                initView(song)
            }
        }
    }

    private fun initView(song: Song) {
        binding?.tvNameSong?.text = song.nameSong
        binding?.tvArtistName?.text = song.artistSong
        binding?.ivPlaySong?.let {
            Glide.with(this)
                .load(song.imageSong)
                .placeholder(android.R.color.darker_gray)
                .into(it)
        }
    }

    private fun init(){
        //setup actionbar
        setSupportActionBar(binding?.tbPlaySong)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }

    private fun onClick() {
        binding?.tbPlaySong?.setNavigationOnClickListener {
            finish()
        }
        binding?.btnAddTrack?.setOnClickListener{

        }
        binding?.btnNextSong?.setOnClickListener {
            playNextSong()
        }
        binding?.btnPrevSong?.setOnClickListener {
            playPrevSong()

        }
        binding?.btnPlaySong?.setOnClickListener {

        }

    }

    private fun playNextSong() {
        val songSize = songs?.size?.minus(1)
        if(position<songSize!!){
            position+=1
            val song = songs?.get(position)
            if(song!=null){
                initView(song)
            }
        }
    }

    private fun playPrevSong() {
        if(position >0){
            position -= 1
            val song = songs?.get(position)
            if(song!=null){
                initView(song)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val KEY_SONGS = "key_songs"
        const val KEY_POSITION = "key_position"
    }
}
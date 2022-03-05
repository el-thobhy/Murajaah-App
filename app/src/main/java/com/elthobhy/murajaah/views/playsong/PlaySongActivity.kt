package com.elthobhy.murajaah.views.playsong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityPlaySongBinding

class PlaySongActivity : AppCompatActivity() {

    private var _binding: ActivityPlaySongBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlaySongBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        onClick()
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

        }
        binding?.btnPrevSong?.setOnClickListener {

        }
        binding?.btnPlaySong?.setOnClickListener {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
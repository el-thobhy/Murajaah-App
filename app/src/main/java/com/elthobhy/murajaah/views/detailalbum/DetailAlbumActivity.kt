package com.elthobhy.murajaah.views.detailalbum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityDetailAlbumBinding

class DetailAlbumActivity : AppCompatActivity() {

    private var _binding: ActivityDetailAlbumBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailAlbumBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        onClick()
    }

    private fun onClick() {
        binding?.tbDetailAlbum?.setNavigationOnClickListener {
            finish()
        }
    }

    private fun init() {
        //setting Support action bar
        setSupportActionBar(binding?.tbDetailAlbum)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
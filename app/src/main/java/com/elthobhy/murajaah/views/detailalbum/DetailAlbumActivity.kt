package com.elthobhy.murajaah.views.detailalbum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.bumptech.glide.Glide
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.adapter.SongAlbumAdapter
import com.elthobhy.murajaah.databinding.ActivityDetailAlbumBinding
import com.elthobhy.murajaah.models.Album
import com.elthobhy.murajaah.models.Song
import com.elthobhy.murajaah.utils.hide
import com.elthobhy.murajaah.utils.visible
import com.elthobhy.murajaah.views.playsong.PlaySongActivity

class DetailAlbumActivity : AppCompatActivity() {

    private var _binding: ActivityDetailAlbumBinding? = null
    private val binding get() = _binding
    private lateinit var songAlbumAdapter: SongAlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailAlbumBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        showLoading()
        Handler(Looper.getMainLooper()).postDelayed({
            getData()
        },1200)
        onClick()
    }

    private fun getData() {
        if(intent!=null){
            val album = intent.getParcelableExtra<Album>(KEY_ALBUM)
            if(album!=null){
                hideLoading()
                initView(album)
            }else{
                hideLoading()
                Toast.makeText(this, "Data Null",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView(album: Album) {
        binding?.tvNameAlbum?.text = album.nameAlbum
        binding?.tvArtistAlbum?.text = album.artistAlbum
        binding?.tvReleaseAlbum?.text = album.getAlbum()
        binding?.ivDetailAlbum?.let {
            Glide.with(this)
                .load(album.imageAlbum)
                .placeholder(android.R.color.darker_gray)
                .into(it)
        }
        showSongAlbum(album.songs)
    }

    private fun showSongAlbum(songs: List<Song>?) {
        if(songs != null){
            songAlbumAdapter.setData(songs as MutableList<Song>)
            binding?.rvDetailAlbum?.adapter = songAlbumAdapter
        }else{
            Toast.makeText(this, "Data Null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(){
        binding?.swipeDetailAlbum?.visible()
    }

    private fun hideLoading(){
        binding?.swipeDetailAlbum?.hide()
    }

    private fun onClick() {
        binding?.tbDetailAlbum?.setNavigationOnClickListener {
            finish()
        }

        songAlbumAdapter.onClick{songs , position ->
            val intent = Intent(this, PlaySongActivity::class.java)
            intent.putParcelableArrayListExtra(PlaySongActivity.KEY_SONGS, ArrayList(songs))
            intent.putExtra(PlaySongActivity.KEY_POSITION, position)
            startActivity(intent)
        }
    }

    private fun init() {
        //setting Support action bar
        setSupportActionBar(binding?.tbDetailAlbum)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        //song Album adapter
        songAlbumAdapter = SongAlbumAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val KEY_ALBUM = "key_album"
    }
}
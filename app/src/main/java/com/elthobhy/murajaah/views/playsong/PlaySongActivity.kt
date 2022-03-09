package com.elthobhy.murajaah.views.playsong

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityPlaySongBinding
import com.elthobhy.murajaah.models.Song
import com.elthobhy.murajaah.utils.toSongTime
import java.lang.IllegalStateException

class PlaySongActivity : AppCompatActivity() {

    private var _binding: ActivityPlaySongBinding? = null
    private val binding get() = _binding
    private var position = 0
    private var songs: MutableList<Song>? = null
    private var musicPlayer: MediaPlayer? = null
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlaySongBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        getData()
        onClick()
    }

    override fun onPause() {
        super.onPause()
        musicPlayer?.pause()
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
        playSong(song)
    }

    private fun playSong(song: Song) {
        try {
            musicPlayer?.setDataSource(song.uriSong)
            musicPlayer?.setOnPreparedListener {
                it.start()
                binding?.tvStartEnd?.text = it?.duration?.toSongTime()
                binding?.seekBarPlaySong?.max = it?.duration!!
                checkMusicButton()
            }
            musicPlayer?.prepareAsync()
            handler.postDelayed(object : Runnable{
                override fun run() {
                    try {
                        binding?.seekBarPlaySong?.progress = musicPlayer?.currentPosition!!
                        handler.postDelayed(this, 1000)
                    }catch (e:Exception){
                        Log.e("error", "run: ${e.message}", )
                    }
                }

            }, 0)
            binding?.seekBarPlaySong?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(musicPlayer!=null){
                        val currentTime = progress.toSongTime()
                        binding?.tvStartTime?.text = currentTime

                        if(!musicPlayer?.isPlaying!!){
                            binding?.tvStartTime?.text = musicPlayer?.currentPosition?.toSongTime()
                        }

                        if(fromUser){
                            musicPlayer?.seekTo(progress)
                            seekBar?.progress = progress
                        }
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })
        }catch (e:IllegalStateException){
            Log.e("playSong", "playSong: ${e.message}", )
        }
    }

    private fun checkMusicButton() {
        if(musicPlayer?.isPlaying == true){
            binding?.btnPlaySong?.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_pause))
        }else{
            binding?.btnPlaySong?.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_play_circle_80))
        }
    }

    private fun init(){
        handler = Handler(Looper.getMainLooper())
        musicPlayer = MediaPlayer()
        musicPlayer?.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

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
            if(musicPlayer?.isPlaying==true){
                musicPlayer?.pause()
                binding?.btnPlaySong?.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_play_circle_80))
            }else{
                musicPlayer?.start()
                binding?.btnPlaySong?.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_pause))
            }
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
        musicPlayer?.stop()
        musicPlayer=null
    }

    override fun onResume() {
        super.onResume()
        if(musicPlayer!=null && !musicPlayer?.isPlaying!!){
            musicPlayer?.start()
        }
    }

    companion object{
        const val KEY_SONGS = "key_songs"
        const val KEY_POSITION = "key_position"
    }
}
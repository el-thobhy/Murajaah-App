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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.lang.IllegalStateException

class PlaySongActivity : AppCompatActivity() {

    private var _binding: ActivityPlaySongBinding? = null
    private val binding get() = _binding
    private var position = 0
    private var songs: MutableList<Song>? = null
    private var musicPlayer: MediaPlayer? = null
    private lateinit var handler: Handler
    private var currentUser: FirebaseUser? = null
    private lateinit var databaseMyTrack : DatabaseReference
    private var isMyTrack = false

    private val eventListenerCheckTrack = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val song = songs?.get(position)
            if (snapshot.value != null){
                for(snap in snapshot.children){
                    if (snap.key == song?.keySong){
                        isMyTrack=true
                        checkLikeButton()
                        break
                    }else{
                        isMyTrack=false
                        checkLikeButton()
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("error", "onCancelled: ${error.message}", )
        }
    }

    private val evenListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val data = snapshot.value
            if(data!=null){
                val song = songs?.get(position)
                databaseMyTrack
                    .child(currentUser?.uid.toString())
                    .child("my_tracks")
                    .child(song?.keySong.toString())
                    .setValue(song)

                isMyTrack = true
                checkLikeButton()
            }else{
                val song = songs?.get(position)
                databaseMyTrack
                    .child(currentUser?.uid.toString())
                    .child("my_tracks")
                    .child(song?.keySong.toString())
                    .setValue(song)

                isMyTrack = true
                checkLikeButton()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("error", "onDataChange: ${error.message}", )
        }
    }

    private fun checkLikeButton() {
        if(isMyTrack){
            binding?.btnAddTrack?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_playlist_add_check_24))
            binding?.btnAddTrack?.setColorFilter(ContextCompat.getColor(this, android.R.color.white))
        }else{
            binding?.btnAddTrack?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_playlist_add_24))
            binding?.btnAddTrack?.setColorFilter(ContextCompat.getColor(this, android.R.color.darker_gray))
        }
    }

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
        //get data mutrack
        checkMytrack()
    }

    private fun checkMytrack() {
        databaseMyTrack
            .child(currentUser?.uid.toString())
            .child("my_tracks")
            .addListenerForSingleValueEvent(eventListenerCheckTrack)
    }

    private fun initView(song: Song) {
        checkButtonPrevNext()
        binding?.tvNameSong?.text = song.nameSong
        binding?.tvArtistName?.text = song.artistSong
        binding?.ivPlaySong?.let {
            Glide.with(this)
                .load(song.imageSong)
                .placeholder(android.R.color.darker_gray)
                .into(it)
        }
//        playSong(song)
    }

    private fun checkButtonPrevNext() {
        if(position==0){
            binding?.btnPrevSong?.setColorFilter(ContextCompat.getColor(this,android.R.color.darker_gray))
            binding?.btnPrevSong?.isEnabled = false
        }else if(position>0 && position<songs?.size?.minus(1)!!){
            binding?.btnPrevSong?.setColorFilter(ContextCompat.getColor(this,android.R.color.white))
            binding?.btnPrevSong?.isEnabled = true

            binding?.btnNextSong?.setColorFilter(ContextCompat.getColor(this,android.R.color.white))
            binding?.btnNextSong?.isEnabled = true
        }else{
            binding?.btnNextSong?.setColorFilter(ContextCompat.getColor(this,android.R.color.darker_gray))
            binding?.btnNextSong?.isEnabled = false
        }
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
                        val maxDuration = musicPlayer?.duration
                        binding?.tvStartTime?.text = currentTime

                        if(!musicPlayer?.isPlaying!!){
                            binding?.tvStartTime?.text = musicPlayer?.currentPosition?.toSongTime()
                        }

                        if(progress==maxDuration){
                            playNextSong()
                        }

                        if(musicPlayer?.isPlaying ==true){
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
        if(musicPlayer?.isPlaying == true && musicPlayer != null){
            binding?.btnPlaySong?.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.icon_pause))
        }else{
            binding?.btnPlaySong?.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_baseline_play_circle_80))
        }
    }

    private fun init(){
        currentUser = FirebaseAuth.getInstance().currentUser
        databaseMyTrack = FirebaseDatabase
            .getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")

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
            if(isMyTrack){
                removeMyTrack()
            }else{
                addMyTrack()
            }
        }
        binding?.btnNextSong?.setOnClickListener {
            playNextSong()
            checkMytrack()
        }
        binding?.btnPrevSong?.setOnClickListener {
            playPrevSong()
            checkMytrack()

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

    private fun removeMyTrack() {
        val song = songs?.get(position)
        databaseMyTrack
            .child(currentUser?.uid.toString())
            .child("my_tracks")
            .child(song?.keySong.toString())
            .removeValue()
        isMyTrack =false
        checkLikeButton()
    }

    private fun addMyTrack() {
        databaseMyTrack
            .child(currentUser?.uid.toString())
            .child("my_tracks")
            .addListenerForSingleValueEvent(evenListener)
    }

    private fun playNextSong() {
        val songSize = songs?.size?.minus(1)
        if(position<songSize!!){
            position+=1
            val song = songs?.get(position)
            musicPlayer?.reset()
            if(song!=null){
                initView(song)
            }
        }
    }

    private fun playPrevSong() {
        if(position >0){
            position -= 1
            val song = songs?.get(position)
            musicPlayer?.reset()
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
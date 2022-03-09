package com.elthobhy.murajaah.views.topalbum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.adapter.TopAlbumAdapter
import com.elthobhy.murajaah.adapter.TopChartsAdapter
import com.elthobhy.murajaah.databinding.FragmentTopAlbumBinding
import com.elthobhy.murajaah.models.Album
import com.elthobhy.murajaah.models.Song
import com.elthobhy.murajaah.repository.Repository
import com.elthobhy.murajaah.utils.hide
import com.elthobhy.murajaah.utils.visible
import com.elthobhy.murajaah.views.detailalbum.DetailAlbumActivity
import com.elthobhy.murajaah.views.playsong.PlaySongActivity
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TopAlbumFragment : Fragment() {

    private var _binding: FragmentTopAlbumBinding? = null
    private val binding get() = _binding
    private lateinit var topAlbumAdapter: TopAlbumAdapter
    private lateinit var databaseTopCharts: DatabaseReference

    private val eventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            Log.e("cek data", "onDataChange: ${snapshot.value}", )
            val gson = Gson().toJson(snapshot.value)
            val type = object : TypeToken<MutableList<Album>>(){}.type
            val song = Gson().fromJson<MutableList<Album>>(gson, type)

            if(song!=null){
                topAlbumAdapter.setData(song)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("check", "onCancelled: ${error.message}", )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTopAlbumBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topAlbumAdapter = TopAlbumAdapter()
        databaseTopCharts = FirebaseDatabase
            .getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("top_albums")

        swipeTopAlbum()
        onClick()

        showLoading()
        showTopAlbums()
    }

    private fun swipeTopAlbum() {
        binding?.swipeTopAlbums?.setOnRefreshListener {
            showTopAlbums()
        }
    }

    private fun onClick() {
        topAlbumAdapter.onClick{albums->
            val intent = Intent(context, DetailAlbumActivity::class.java)
            intent.putExtra(DetailAlbumActivity.KEY_ALBUM, albums)
            startActivity(intent)
        }


    }

    private fun hideLoading() {
        binding?.swipeTopAlbums?.hide()
    }

    private fun showLoading() {
        binding?.swipeTopAlbums?.visible()
    }

    private fun showTopAlbums() {
        databaseTopCharts.addValueEventListener(eventListener)
        //setup recycler View
        binding?.rvTopAlbums?.adapter = topAlbumAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
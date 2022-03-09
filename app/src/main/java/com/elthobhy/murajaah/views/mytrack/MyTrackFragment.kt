package com.elthobhy.murajaah.views.mytrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elthobhy.murajaah.adapter.MyTracksAdapter
import com.elthobhy.murajaah.databinding.FragmentMyTrackBinding
import com.elthobhy.murajaah.models.Song
import com.elthobhy.murajaah.repository.Repository
import com.elthobhy.murajaah.utils.gone
import com.elthobhy.murajaah.utils.hide
import com.elthobhy.murajaah.utils.visible
import com.elthobhy.murajaah.views.playsong.PlaySongActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyTrackFragment : Fragment() {

    private var _binding: FragmentMyTrackBinding? = null
    private val binding get() = _binding
    private lateinit var myTracksAdapter: MyTracksAdapter
    private var currentUser: FirebaseUser? = null
    private lateinit var databaseMyTrack: DatabaseReference

    private val eventListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            val gson = Gson().toJson(snapshot.value)
            val type = object : TypeToken<MutableList<Song>>(){}.type
            val myTracks = Gson().fromJson<MutableList<Song>>(gson, type)

            if(myTracks!=null){
                hideEmptyData()
                myTracksAdapter.setData(myTracks)
            }else{
                showEmptyData()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            hideLoading()
            Log.e("error", "onCancelled: ${error.message}", )
        }
    }

    private fun showEmptyData() {
        binding?.rvMyTracks?.gone()
        binding?.emptyData?.visible()
    }

    private fun hideEmptyData() {
        binding?.rvMyTracks?.visible()
        binding?.emptyData?.gone()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyTrackBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init
        currentUser = FirebaseAuth.getInstance().currentUser
        databaseMyTrack = FirebaseDatabase
            .getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")
        myTracksAdapter = MyTracksAdapter()
        swipeMyTracks()
        onClick()

        showLoading()
        showMyTracks()
    }

    private fun onClick() {
        myTracksAdapter.onClick{songs, position->
            val intent = Intent(context, PlaySongActivity::class.java)
            intent.putParcelableArrayListExtra(PlaySongActivity.KEY_SONGS, ArrayList(songs))
            intent.putExtra(PlaySongActivity.KEY_POSITION, position)
            startActivity(intent)
        }
    }

    private fun swipeMyTracks() {
        binding?.swipeMyTracks?.setOnRefreshListener {
            showMyTracks()
        }
    }

    private fun showMyTracks() {
        hideLoading()
//        //GetData
//        val myTrack = Repository.getFataTopChartFromAssets(context)
//        myTracksAdapter.setData(myTrack as MutableList<Song>)

        //get Data from firebase
        databaseMyTrack
            .child(currentUser?.uid.toString())
            .child("my_tracks")
            .addValueEventListener(eventListener)
        //setup recycler View
        binding?.rvMyTracks?.adapter = myTracksAdapter
    }

    private fun hideLoading() {
        binding?.swipeMyTracks?.hide()
    }

    private fun showLoading() {
        binding?.swipeMyTracks?.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
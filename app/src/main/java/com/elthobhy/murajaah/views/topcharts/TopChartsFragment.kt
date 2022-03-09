package com.elthobhy.murajaah.views.topcharts

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.adapter.TopChartsAdapter
import com.elthobhy.murajaah.databinding.FragmentTopChartsBinding
import com.elthobhy.murajaah.models.Song
import com.elthobhy.murajaah.repository.Repository
import com.elthobhy.murajaah.utils.hide
import com.elthobhy.murajaah.utils.visible
import com.elthobhy.murajaah.views.playsong.PlaySongActivity
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TopChartsFragment : Fragment() {

    private var _binding: FragmentTopChartsBinding? = null
    private val binding get() = _binding
    private lateinit var topChartsAdapter :TopChartsAdapter
    private lateinit var databaseTopCharts: DatabaseReference

    private val eventListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            hideLoading()
            Log.e("cek data", "onDataChange: ${snapshot.value}", )
            val gson = Gson().toJson(snapshot.value)
            val type = object : TypeToken<MutableList<Song>>(){}.type
            val song = Gson().fromJson<MutableList<Song>>(gson, type)

            if(song!=null){
                topChartsAdapter.setData(song)
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
        _binding = FragmentTopChartsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init
        topChartsAdapter = TopChartsAdapter()
        databaseTopCharts = FirebaseDatabase
            .getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("top_charts")

        showLoading()
        swipeTopChart()
        showTopCharts()
        onClick()

    }

    private fun onClick() {
        topChartsAdapter.onClick{songs, position ->
            val intent = Intent(context, PlaySongActivity::class.java)
            intent.putParcelableArrayListExtra(PlaySongActivity.KEY_SONGS, ArrayList(songs))
            intent.putExtra(PlaySongActivity.KEY_POSITION, position)
            startActivity(intent)
        }
    }

    private fun swipeTopChart() {
        binding?.swipeTopCharts?.setOnRefreshListener {
            showTopCharts()
        }
    }

    private fun showTopCharts() {
        //GetData from firebase
        databaseTopCharts.addValueEventListener(eventListener)
        //setup recycler View
        binding?.rvTopCharts?.adapter = topChartsAdapter
    }

    private fun hideLoading() {
        binding?.swipeTopCharts?.hide()
    }

    private fun showLoading() {
        binding?.swipeTopCharts?.visible()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
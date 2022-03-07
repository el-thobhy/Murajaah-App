package com.elthobhy.murajaah.views.mytrack

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elthobhy.murajaah.adapter.MyTracksAdapter
import com.elthobhy.murajaah.databinding.FragmentMyTrackBinding
import com.elthobhy.murajaah.models.Song
import com.elthobhy.murajaah.repository.Repository
import com.elthobhy.murajaah.utils.hide
import com.elthobhy.murajaah.utils.visible
import com.elthobhy.murajaah.views.playsong.PlaySongActivity

class MyTrackFragment : Fragment() {

    private var _binding: FragmentMyTrackBinding? = null
    private val binding get() = _binding
    private lateinit var myTracksAdapter: MyTracksAdapter

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

        myTracksAdapter = MyTracksAdapter()
        swipeMyTracks()
        onClick()

        showLoading()
        showMyTracks()
    }

    private fun onClick() {
        myTracksAdapter.onClick{songs, position->
            startActivity(Intent(context, PlaySongActivity::class.java))
        }
    }

    private fun swipeMyTracks() {
        binding?.swipeMyTracks?.setOnRefreshListener {
            showMyTracks()
        }
    }

    private fun showMyTracks() {
        hideLoading()
        //GetData
        val myTrack = Repository.getFataTopChartFromAssets(context)
        myTracksAdapter.setData(myTrack as MutableList<Song>)

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
package com.elthobhy.murajaah.views.topcharts

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class TopChartsFragment : Fragment() {

    private var _binding: FragmentTopChartsBinding? = null
    private val binding get() = _binding
    private lateinit var topChartsAdapter :TopChartsAdapter

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

        showLoading()
        swipeTopChart()
        Handler(Looper.getMainLooper()).postDelayed({
            showTopCharts()
        },2000)

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
        hideLoading()
        //GetData
        val topChart = Repository.getFataTopChartFromAssets(context)
        topChartsAdapter.setData(topChart as MutableList<Song>)

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
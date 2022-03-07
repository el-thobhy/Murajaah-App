package com.elthobhy.murajaah.views.topalbum

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.adapter.TopAlbumAdapter
import com.elthobhy.murajaah.databinding.FragmentTopAlbumBinding
import com.elthobhy.murajaah.models.Album
import com.elthobhy.murajaah.models.Song
import com.elthobhy.murajaah.repository.Repository
import com.elthobhy.murajaah.utils.hide
import com.elthobhy.murajaah.utils.visible
import com.elthobhy.murajaah.views.detailalbum.DetailAlbumActivity
import com.elthobhy.murajaah.views.playsong.PlaySongActivity

class TopAlbumFragment : Fragment() {

    private var _binding: FragmentTopAlbumBinding? = null
    private val binding get() = _binding
    private lateinit var topAlbumAdapter: TopAlbumAdapter

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
        hideLoading()
        //GetData
        val topAlbum = Repository.getFataTopAlbumFromAssets(context)
        topAlbumAdapter.setData(topAlbum as MutableList<Album>)

        //setup recycler View
        binding?.rvTopAlbums?.adapter = topAlbumAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
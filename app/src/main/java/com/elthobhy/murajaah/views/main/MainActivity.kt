package com.elthobhy.murajaah.views.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityMainBinding
import com.elthobhy.murajaah.views.mytrack.MyTrackFragment
import com.elthobhy.murajaah.views.topalbum.TopAlbumFragment
import com.elthobhy.murajaah.views.topcharts.TopChartsFragment
import com.elthobhy.murajaah.views.user.UserFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setup()

    }

    private fun setup() {
        //setup Navigation Bar
        binding?.btmNavigationMain?.setOnItemSelectedListener {id->
            when(id){
                R.id.action_my_tracks -> openFragment(MyTrackFragment())
                R.id.action_top_albums -> openFragment(TopAlbumFragment())
                R.id.action_top_charts -> openFragment(TopChartsFragment())
                R.id.action_user -> openFragment(UserFragment())
            }
        }
        openHomeFragment()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val setSelected = binding?.btmNavigationMain?.getSelectedItemId()
        if(setSelected == R.id.action_top_charts){
            finishAffinity()
        }else{
            openHomeFragment()
        }
    }

    private fun openHomeFragment() {
        binding?.btmNavigationMain?.setItemSelected(R.id.action_top_charts)
    }

    private fun openFragment(fragment: Fragment) {
        //setup fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_main, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
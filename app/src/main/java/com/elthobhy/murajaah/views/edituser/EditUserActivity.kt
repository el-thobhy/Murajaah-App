package com.elthobhy.murajaah.views.edituser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityEditUserBinding

class EditUserActivity : AppCompatActivity() {

    private var _binding: ActivityEditUserBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        onClick()
    }

    private fun onClick() {
        binding?.btnUpdate?.setOnClickListener {
            Toast.makeText(this, "Berhasil diupdate", Toast.LENGTH_SHORT).show()
        }
        binding?.tbEditUser?.setNavigationOnClickListener {
            finish()
        }
    }

    private fun init() {
        //setSupport action bar
        setSupportActionBar(binding?.tbEditUser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
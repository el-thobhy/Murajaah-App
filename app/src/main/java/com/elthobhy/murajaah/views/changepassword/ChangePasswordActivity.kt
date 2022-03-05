package com.elthobhy.murajaah.views.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private var _binding: ActivityChangePasswordBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
        onClick()
    }

    private fun onClick() {
        binding?.tbChangePassword?.setNavigationOnClickListener {
            finish()
        }
        binding?.btnUpdate?.setOnClickListener {
            Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init(){
        //setting action bar
        setSupportActionBar(binding?.tbChangePassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
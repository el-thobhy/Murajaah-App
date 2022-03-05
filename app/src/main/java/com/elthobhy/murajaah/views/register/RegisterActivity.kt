package com.elthobhy.murajaah.views.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //init
        init()
        onClick()
    }

    private fun onClick() {
        binding?.btnAlreadyMemberLogin?.setOnClickListener {
            finish()
        }
        binding?.btnRegister?.setOnClickListener {
            Toast.makeText(this, "register", Toast.LENGTH_SHORT).show()
        }
        binding?.tbRegister?.setNavigationOnClickListener {
            finish()
        }
    }

    private fun init() {
        //setup action bar
        setSupportActionBar(binding?.tbRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
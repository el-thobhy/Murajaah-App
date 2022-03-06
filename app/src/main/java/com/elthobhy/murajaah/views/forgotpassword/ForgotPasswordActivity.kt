package com.elthobhy.murajaah.views.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private var _binding: ActivityForgotPasswordBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        //init
        init()
        onClick()

    }

    private fun onClick() {
        binding?.tbForgotPassword?.setNavigationOnClickListener {
            finish()
        }

        binding?.btnForgotPassword?.setOnClickListener {

        }
    }

    private fun init() {
        //setup action bar
        setSupportActionBar(binding?.tbForgotPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
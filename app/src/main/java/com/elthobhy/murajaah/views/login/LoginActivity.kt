package com.elthobhy.murajaah.views.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityLoginBinding
import com.elthobhy.murajaah.views.forgotpassword.ForgotPasswordActivity
import com.elthobhy.murajaah.views.main.MainActivity
import com.elthobhy.murajaah.views.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        onCLick()

    }

    private fun onCLick() {
        binding?.btnLogin?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding?.btnNewRegister?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding?.btnForgotPassword?.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
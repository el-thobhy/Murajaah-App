package com.elthobhy.murajaah.views.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityForgotPasswordBinding
import com.elthobhy.murajaah.utils.gone
import com.elthobhy.murajaah.utils.visible
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private var _binding: ActivityForgotPasswordBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

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
            val email = binding?.etEmail?.text.toString().trim()

            if(validationChcek(email)){
                forgotPasswordToServer(email)
            }
        }
    }

    private fun forgotPasswordToServer(email: String) {
        showLoading()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    hideLoading()
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.succes))
                        .setMessage(getString(R.string.link_sent))
                        .show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    },1500)
                }
            }
            .addOnFailureListener {
                hideLoading()
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.erro))
                    .setMessage(it.message)
                    .show()
            }
    }

    private fun hideLoading() {
        binding?.pbForgotPassword?.gone()
    }

    private fun showLoading() {
        binding?.pbForgotPassword?.visible()
    }

    private fun validationChcek(email: String): Boolean {
        if(email.isEmpty()){
            binding?.etEmail?.error = getString(R.string.email_empty_error)
            binding?.etEmail?.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding?.etEmail?.error = getString(R.string.please_valid_email)
            binding?.etEmail?.requestFocus()
        }else{
            return true
        }
        return false
    }

    private fun init() {
        //setup action bar
        setSupportActionBar(binding?.tbForgotPassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        auth = FirebaseAuth.getInstance()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
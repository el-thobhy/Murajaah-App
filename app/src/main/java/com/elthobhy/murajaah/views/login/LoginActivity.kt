package com.elthobhy.murajaah.views.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityLoginBinding
import com.elthobhy.murajaah.utils.gone
import com.elthobhy.murajaah.utils.visible
import com.elthobhy.murajaah.views.forgotpassword.ForgotPasswordActivity
import com.elthobhy.murajaah.views.main.MainActivity
import com.elthobhy.murajaah.views.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()
        checkIfAlreadyLogin()

        onCLick()

    }

    private fun checkIfAlreadyLogin() {
        val currentUser = auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    private fun onCLick() {
        binding?.btnLogin?.setOnClickListener {
            val email = binding?.etEmail?.text.toString().trim()
            val password = binding?.etPassword?.text.toString().trim()

            if(checkValidation(email,password)){
                loginToServer(email, password)
            }
        }
        binding?.btnNewRegister?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding?.btnForgotPassword?.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }

    private fun loginToServer(email: String, password: String) {
        showLoading()
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                hideLoading()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage(it.message)
                    .setTitle(getString(R.string.erro))
                    .show()
                hideLoading()
            }
    }

    private fun hideLoading() {
        binding?.pbLogin?.gone()
    }

    private fun showLoading() {
        binding?.pbLogin?.visible()
    }

    private fun checkValidation(email: String, password: String): Boolean {
        if(email.isEmpty()){
            binding?.etEmail?.error = getString(R.string.email_empty_error)
            binding?.etEmail?.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding?.etEmail?.error = getString(R.string.please_valid_email)
            binding?.etEmail?.requestFocus()
        }else if(password.isEmpty()){
            binding?.etPassword?.error = getString(R.string.error_password_empty)
            binding?.etPassword?.requestFocus()
        }else{
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
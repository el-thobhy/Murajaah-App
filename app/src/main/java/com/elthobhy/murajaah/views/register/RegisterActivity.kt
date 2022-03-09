package com.elthobhy.murajaah.views.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityRegisterBinding
import com.elthobhy.murajaah.models.User
import com.elthobhy.murajaah.utils.gone
import com.elthobhy.murajaah.utils.visible
import com.elthobhy.murajaah.views.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private lateinit var userDatabase: DatabaseReference

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
            val fullName = binding?.etFullName?.text.toString().trim()
            val email = binding?.etEmail?.text.toString().trim()
            val password = binding?.etPassword?.text.toString().trim()
            val confirm_password = binding?.etConfirmPassword?.text.toString().trim()

            if(validationCheck(fullName, email, password, confirm_password)){
                registerToServer(fullName, email, password)
            }
        }
        binding?.tbRegister?.setNavigationOnClickListener {
            finish()
        }
    }

    private fun registerToServer(fullName: String, email: String, password: String) {
        showLoading()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult->
                if(authResult.isSuccessful){
                    val uid = auth.currentUser?.uid
                    val user = User(
                        fullName = fullName,
                        email = email,
                        uid = uid
                    )

                    userDatabase.child(uid.toString()).setValue(user)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                hideLoading()
                                AlertDialog.Builder(this)
                                    .setTitle(getString(R.string.succes))
                                    .setMessage(getString(R.string.account_created))
                                    .show()

                                Handler(Looper.getMainLooper()).postDelayed({
                                    startActivity(Intent(this, LoginActivity::class.java))
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
        binding?.pbRegister?.gone()
    }

    private fun showLoading() {
        binding?.pbRegister?.visible()
    }

    private fun validationCheck(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if(fullName.isEmpty()){
            binding?.etFullName?.error = getString(R.string.please_field_fullname)
            binding?.etFullName?.requestFocus()
        }else if(email.isEmpty()){
            binding?.etEmail?.error = getString(R.string.email_empty_error)
            binding?.etEmail?.requestFocus()
        }else if(password.isEmpty()){
            binding?.etPassword?.error = getString(R.string.error_password_empty)
            binding?.etPassword?.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding?.etEmail?.error = getString(R.string.please_valid_email)
            binding?.etEmail?.requestFocus()
        }else if(confirmPassword.isEmpty()){
            binding?.etConfirmPassword?.error = getString(R.string.error_password_empty)
            binding?.etConfirmPassword?.requestFocus()
        }else if(password!=confirmPassword){
            binding?.etConfirmPassword?.error = getString(R.string.different_confirm_password)
        }else{
            return true
        }
        return false
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        userDatabase = FirebaseDatabase
            .getInstance("https://murajaah-f040b-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("users")

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
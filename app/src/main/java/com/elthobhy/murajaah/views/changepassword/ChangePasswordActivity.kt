package com.elthobhy.murajaah.views.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.ActivityChangePasswordBinding
import com.elthobhy.murajaah.utils.gone
import com.elthobhy.murajaah.utils.visible
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ChangePasswordActivity : AppCompatActivity() {

    private var _binding: ActivityChangePasswordBinding? = null
    private val binding get() = _binding
    private lateinit var currentUser: FirebaseUser

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
            val oldPassword = binding?.etOldPassword?.text.toString().trim()
            val newPassword = binding?.etNewPassword?.text.toString().trim()

            if(checkValidation(oldPassword, newPassword)){
                changePasswordToServer(oldPassword, newPassword)
            }
        }
    }

    private fun changePasswordToServer(oldPassword: String, newPassword: String) {
        showLoading()
        val  credential = EmailAuthProvider.getCredential(currentUser.email.toString(), oldPassword)
        currentUser.reauthenticate(credential)
            .addOnSuccessListener {
                currentUser.updatePassword(newPassword)
                    .addOnSuccessListener {
                        hideLoading()
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.succes))
                            .setMessage(getString(R.string.succes_change_pass))
                            .show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            finish()
                        },1400)
                    }
                    .addOnFailureListener {
                        hideLoading()
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.erro))
                            .setMessage(it.message)
                            .show()
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
        binding?.pbChangePassword?.gone()
    }

    private fun showLoading() {
        binding?.pbChangePassword?.visible()
    }

    private fun checkValidation(oldPassword: String, newPassword: String): Boolean {
        if(oldPassword.isEmpty()){
            binding?.etOldPassword?.error = getString(R.string.error_password_empty)
            binding?.etOldPassword?.requestFocus()
        }else if(newPassword.isEmpty()){
            binding?.etNewPassword?.error = getString(R.string.error_password_empty)
            binding?.etNewPassword?.requestFocus()
        }else{
            return true
        }
        return false
    }

    private fun init(){
        //setting action bar
        setSupportActionBar(binding?.tbChangePassword)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        currentUser = FirebaseAuth.getInstance().currentUser!!
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
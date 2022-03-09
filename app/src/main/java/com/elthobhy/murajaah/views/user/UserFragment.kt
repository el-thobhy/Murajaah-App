package com.elthobhy.murajaah.views.user

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.databinding.FragmentUserBinding
import com.elthobhy.murajaah.views.changepassword.ChangePasswordActivity
import com.elthobhy.murajaah.views.edituser.EditUserActivity
import com.elthobhy.murajaah.views.login.LoginActivity
import com.elthobhy.murajaah.views.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        onClick()
    }

    private fun onClick() {
        binding?.btnEditUser?.setOnClickListener {
            startActivity(Intent(context, EditUserActivity::class.java))
        }
        binding?.btnChangePassword?.setOnClickListener {
            startActivity(Intent(context, ChangePasswordActivity::class.java))
        }
        binding?.btnLogout?.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context,LoginActivity::class.java))
            (activity as MainActivity).finishAffinity()
        }
        binding?.btnChangeLanguage?.setOnClickListener {
            startActivity(Intent(ACTION_LOCALE_SETTINGS))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
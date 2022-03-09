package com.elthobhy.murajaah.views.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.elthobhy.murajaah.R
import com.elthobhy.murajaah.repository.Repository
import com.elthobhy.murajaah.views.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //menambahkan dataTopCharts ke realtime firebase
        //Repository.addDataToTopCharts()
        //Menambahkna image ke dataTopCharts
        Repository.addDataImageToTopCharts()
        goToLogin()
    }

    private fun goToLogin() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        },1200)
    }
}
package com.abilsayuri.flix.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.abilsayuri.flix.R
import com.abilsayuri.flix.local.UserPreferences

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed(
            {
                val database = UserPreferences(this)
                val user = database.getUser().username

                if (user != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish();
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish();
                }

            }, 3000
        )
    }
}
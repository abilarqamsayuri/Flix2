package com.abilsayuri.flix.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abilsayuri.flix.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            submitLogin()
        }

        binding.btnRegister.setOnClickListener {
            submitRegister()
        }
    }

    private fun submitRegister() {
        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun submitLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
package com.submission.tesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.submission.tesapp.MainActivity
import com.submission.tesapp.R
import com.submission.tesapp.data.preference.UserSessionManager
import com.submission.tesapp.databinding.ActivityLoginBinding
import com.submission.tesapp.firebase.auth.FirebaseLoginManager

class LoginActivity : AppCompatActivity() {
    private lateinit var authManager: FirebaseLoginManager
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userSessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = com.submission.tesapp.databinding.ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        authManager = FirebaseLoginManager(this)
        userSessionManager = UserSessionManager(this)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill in your email and password!", Toast.LENGTH_SHORT).show()
            } else {
                authManager.loginUser(email, password,
                    onSuccess = {
                        // Login berhasil
                        userSessionManager.setUserLoggedIn(true)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onFailure = {
                        val builder = AlertDialog.Builder(this)
                        val alert = builder.create()
                        builder
                            .setMessage("Login failed. Please check again your email and password!")
                            .setPositiveButton("OK") { _, _ ->
                                alert.cancel()
                            }.show()
                    }
                )
            }
        }
    }
}
package com.submission.appriori.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.submission.appriori.R
import com.submission.appriori.databinding.ActivityForgetPasswordBinding
import com.submission.appriori.firebase.auth.FirebaseLoginManager

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var authManager: FirebaseLoginManager

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.lupa_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        authManager = FirebaseLoginManager(this)
        binding.btnForgotPassword.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            if (isValidEmail(email)) {
                checkIfEmailRegistered(email)
            } else {
                showToast(getString(R.string.message_invalid))
            }
            sendPasswordResetEmail(email)
        }
    }



    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkIfEmailRegistered(email: String) {
        isLoading(true)
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                isLoading(false)
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    Log.d("ForgotPasswordActivity", "SignInMethods: ${signInMethods?.joinToString()}")
                    if (signInMethods.isNullOrEmpty()) {
                        // Jika email tidak terdaftar, tampilkan pesan
                        showToast(getString(R.string.message_email_not_registered))
                    } else {
                        // Jika email terdaftar, kirim email reset password
                        sendPasswordResetEmail(email)
                    }
                } else {
                    Log.e("ForgotPasswordActivity", "Error fetching sign-in methods", task.exception)
                    showToast(getString(R.string.message_error_forgotpw))
                }
            }
    }

    private fun sendPasswordResetEmail(email: String) {
        isLoading(true)
        authManager.forgotPassword(email,
            onSuccess = {
                isLoading(false)
                showToast(getString(R.string.message_forgot_password, email))
            },
            onFailure = {
                isLoading(false)
                Log.e("ForgotPasswordActivity", "Error sending reset email")
                showToast(getString(R.string.message_error_forgotpw))
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Loading...")
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
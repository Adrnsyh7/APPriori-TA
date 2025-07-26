package com.submission.appriori.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.submission.appriori.R
import com.submission.appriori.data.preference.UserSessionManager
import com.submission.appriori.firebase.auth.FirebaseLoginManager
import com.submission.appriori.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var authManager: FirebaseLoginManager
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userSessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        authManager = FirebaseLoginManager(this)
        auth = Firebase.auth
        userSessionManager = UserSessionManager(this)

        binding.google.setOnClickListener {
            signIn()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            binding.progressBarLogin.visibility = View.VISIBLE
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                binding.progressBarLogin.visibility = View.GONE
                Toast.makeText(this, "Silahkan isi email dan password!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                authManager.loginUser(email, password,
                    onSuccess = {
                        // Login berhasil
                        binding.progressBarLogin.visibility = View.GONE
                        userSessionManager.setUserLoggedIn(true)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onFailure = {
                        binding.progressBarLogin.visibility = View.GONE
                        val builder = AlertDialog.Builder(this)
                        val alert = builder.create()
                        builder
                            .setMessage("Login gagal. Silahkan cek kembali email dan password!")
                            .setPositiveButton("OK") { _, _ ->
                                alert.cancel()
                            }.show()
                    }
                )
            }
        }
    }
//
    private fun signIn() {
        val credentialManager =
            CredentialManager.create(this) //import from androidx.CredentialManager
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.your_web_client_id))
            .setAutoSelectEnabled(true)
            .build()
        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    //import from androidx.CredentialManager
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    userSessionManager.setUserLoggedIn(true)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    val builder = AlertDialog.Builder(this)
                    val alert = builder.create()
                    builder
                        .setMessage("Login failed. Please check again your email and password!")
                        .setPositiveButton("OK") { _, _ ->
                            alert.cancel()
                        }.show()
                }
            }
    }


    companion object {
        private const val TAG = "LoginActivity"
    }
}
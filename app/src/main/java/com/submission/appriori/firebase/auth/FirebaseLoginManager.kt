package com.submission.appriori.firebase.auth

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.submission.appriori.R

class FirebaseLoginManager(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
               if(task.isSuccessful) {
                   onSuccess.invoke()
               } else {
                   onFailure.invoke()
            }
        }
    }

    fun forgotPassword(
        email: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    showToast(context.getString(R.string.login_berhasil))
                    onSuccess.invoke()
                }  else {
                    // Login gagal
                    Log.e("FirebaseAuthManager", "Login gagal", task.exception)
                    showToast(context.getString(R.string.login_gagal))
                    onFailure.invoke()
                }

            }
    }

    private fun showToast(message: String) {
        (context as? Activity)?.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
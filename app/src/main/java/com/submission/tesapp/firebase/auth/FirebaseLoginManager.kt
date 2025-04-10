package com.submission.tesapp.firebase.auth

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

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
}
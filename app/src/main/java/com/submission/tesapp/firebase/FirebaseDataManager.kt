package com.submission.tesapp.firebase

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.submission.tesapp.data.model.TransactionModel
import java.util.Calendar

class FirebaseDataManager {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun saveTransaction(item: String, date: Long, callback: (Boolean) -> Unit) {
        val userId = firebaseAuth.currentUser?.uid
        if(userId != null) {
            val transactions = hashMapOf(
                "item" to item,
                "date" to Timestamp(date / 1000, 0)
            )
            firestore.collection("users").document("admin").collection("transactions")
                .add(transactions)
                .addOnSuccessListener {
                    callback.invoke(true)
                }
                .addOnFailureListener {
                    callback.invoke(false)
                    it.printStackTrace()
                }
        } else {
            callback.invoke(false)
        }
    }


}
package com.submission.tesapp.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.submission.tesapp.data.model.TransactionModel
import java.util.Calendar

class FirebaseDataManager {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun saveTransaction(item: String, date: Long, callback: (Boolean) -> Unit) {
    val transactions = hashMapOf(
        "item" to item,
        "date" to Timestamp(date/1000, 0)
    )
    firestore.collection("admin").document().collection("transactions")
        .add(transactions)
        .addOnSuccessListener {
            callback.invoke(true)
        }
        .addOnFailureListener{
            callback.invoke(false)
            it.printStackTrace()
        }

    }

    fun getTransactions(callback: (MutableList<TransactionModel>, Double, Double, Double) -> Unit ){
        val calendar = Calendar.getInstance()
    }
}
package com.submission.tesapp.firebase

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import com.submission.tesapp.data.model.ResultModel
import com.submission.tesapp.data.model.TransactionModel
import java.sql.Time
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

  fun getTransactions(callback: (MutableList<TransactionModel>) -> Unit) {
      val userId = firebaseAuth.currentUser?.uid
      if(userId != null) {
          val txList: ArrayList<TransactionModel> = arrayListOf()
          firestore.collection("users").document("admin").collection("transactions")
              .get()
              .addOnSuccessListener { transactionsSnapshot ->
                  for(document in transactionsSnapshot) {
                      val date = document.getTimestamp("date")?.toDate()
                      val item = document.getString("item")
                      val transaction = TransactionModel(
                          item,
                          date
                      )
                      txList.add(transaction)
                      txList.sortedByDescending { it.date }
                      callback.invoke(txList)
                  }
              }
              .addOnFailureListener{e ->
                  Log.e("FirebaseTransactionManager", "Error getting transactions", e)
              }
      }
  }

    fun getResult(callback: (MutableList<ResultModel>) -> Unit) {
        val resList = mutableListOf<ResultModel>()
        firestore.collection("users").document("admin").collection("result")
            .get()
            .addOnSuccessListener { snapshot ->
                val doc1 = snapshot.toObjects(ResultModel::class.java)
//                for(document in snapshot) {
//                    val data = document.data
//                    val result = ResultModel(
//                        resultId = document.id,
//                        created_at = data["created_at"] as? Timestamp,
//                        from = data["from"] as? Timestamp ?,
//                        end = data["end"] as? Timestamp,
//                        min_support = data["min_support"] as? Double ?: 0.0,
//                        conf = data["conf"] as? Double ?: 0.0
//
//                    )
//                    resList.add(result)
//                    resList.sortedByDescending { it.resultId}
//                    callback.invoke(resList)
//                }
                callback.invoke(doc1)
            }
            .addOnFailureListener{e ->
                Log.e("FirebaseTransactionManager", "Error getting transactions", e)
            }
    }


}
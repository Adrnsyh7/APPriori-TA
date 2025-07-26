package com.submission.appriori.firebase

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.appriori.data.model.ResultModel
import com.submission.appriori.data.model.TransactionModel

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

    fun updateTransactions(id: String, item: String, date: Long, callback: (Boolean) -> Unit) {
        val userId = firebaseAuth.currentUser?.uid
        if(userId != null) {
            firestore.collection("users").document("admin").collection("transactions")
                .document(id).update(
                    mapOf(
                    "item" to item,
                    "date" to Timestamp(date / 1000, 0)
                ))
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

    fun deleteTransactions(id: String, onComplete: (Boolean) -> Unit) {
        firestore.collection("users").document("admin").collection("transactions")
            .document(id).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }

    }

    fun deleteAllTransactions(onComplete: (Boolean) -> Unit) {
        val tx = firestore.collection("users").document("admin").collection("transactions")
        tx.get()
            .addOnSuccessListener {result ->
                for (document in result) {
                    tx.document(document.id).delete()
                }
            }
                .addOnFailureListener { e ->
                    Log.e("FirebaseTransactionManager", "Error delete transactions", e)
                }
    }

    fun deleteAllResult(onComplete: (Boolean) -> Unit) {
        val rst = firestore.collection("users").document("admin").collection("result")
        rst.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    rst.document(document.id).delete()
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseTransactionManager", "Error delete transactions", e)
        }
    }

  fun getTransactions(callback: (MutableList<TransactionModel>) -> Unit) {
      val userId = firebaseAuth.currentUser?.uid
      if(userId != null) {
          val txList = mutableListOf<TransactionModel>()
          firestore.collection("users").document("admin").collection("transactions")
              .get()
              .addOnSuccessListener { transactionsSnapshot ->
                  for(document in transactionsSnapshot) {
                      val id = document.id
                      val date = document.getTimestamp("date")?.toDate()
                      val item = document.getString("item")
                      val transaction = TransactionModel(
                          id,
                          item,
                          date
                      )
                      txList.add(transaction)

                  }
                  txList.sortBy { it.date }
                  callback.invoke(txList)
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
                for(document in snapshot) {
                    val data = document.data
                    val result = ResultModel(
                        resultId = document.id,
                        created_at = data["created_at"] as? Timestamp,
                        from = data["from"] as? Timestamp ?,
                        end = data["end"] as? Timestamp,
                        min_support = data["min_support"] as? Double ?: 0.0,
                        conf = data["conf"] as? Double ?: 0.0
                    )
                    resList.add(result)
                    resList.sortedByDescending { it.resultId}
                    callback.invoke(resList)
                }
                Log.e("tes", snapshot.toString())
            }
            .addOnFailureListener{e ->
                Log.e("FirebaseTransactionManager", "Error getting transactions", e)
            }
    }


}
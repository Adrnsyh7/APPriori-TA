package com.submission.tesapp.ui.report

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.tesapp.R
import com.submission.tesapp.data.model.Itemset1



class ResultDetailActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result_detail)
    }

    private fun loadItemset1(resultId: String) {
         val baseRef = db.collection("users")
            .document("admin")
            .collection("result")
            .document(resultId)
        val i1 = baseRef.collection("itemset1")
        val i2 = baseRef.collection("itemset2")
        val i3 = baseRef.collection("itemset3")
        val i4 = baseRef.collection("assoc_rules")

        i1.get().addOnSuccessListener {

            i2.get().addOnSuccessListener {
                i3.get().addOnSuccessListener {
                    i4.get().addOnSuccessListener {

                    }
                }
            }
        }
    }


    companion object {
        const val resultId = "resultId"
    }

}
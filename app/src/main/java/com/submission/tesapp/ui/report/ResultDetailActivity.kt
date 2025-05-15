package com.submission.tesapp.ui.report

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.tesapp.R
import com.submission.tesapp.adapter.Itemset11Adapter
import com.submission.tesapp.adapter.Itemset21Adapter
import com.submission.tesapp.adapter.Itemset31Adapter
import com.submission.tesapp.adapter.ItemsetAssoc1Adapter
import com.submission.tesapp.adapter.ReportAdapter
import com.submission.tesapp.adapter.ResultFinalAdapter
import com.submission.tesapp.data.model.AssociationRule
import com.submission.tesapp.data.model.Itemset1
import com.submission.tesapp.data.model.Itemset2
import com.submission.tesapp.data.model.Itemset3
import com.submission.tesapp.data.model.ResultModel
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.databinding.ActivityLoginBinding
import com.submission.tesapp.databinding.ActivityResultDetailBinding
import com.submission.tesapp.databinding.FragmentReportBinding
import com.submission.tesapp.firebase.FirebaseDataManager


class ResultDetailActivity : AppCompatActivity() {
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var binding: ActivityResultDetailBinding
    private lateinit var adapter1: Itemset11Adapter
    private lateinit var adapter2: Itemset21Adapter
    private lateinit var adapter3: Itemset31Adapter
    private lateinit var adapter4: ItemsetAssoc1Adapter
    private lateinit var adapter5: ResultFinalAdapter

    private lateinit var firebaseDataManager: FirebaseDataManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = com.submission.tesapp.databinding.ActivityResultDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sv.visibility = View.INVISIBLE
        binding.progressBarProcess.visibility = View.VISIBLE
        firebaseDataManager = FirebaseDataManager()
        adapter1 = Itemset11Adapter()
        adapter2 = Itemset21Adapter()
        adapter3 = Itemset31Adapter()
        adapter4 = ItemsetAssoc1Adapter()
        adapter5 = ResultFinalAdapter()
        val resultsModel: ResultModel? = intent.getParcelableExtra(RESULTID)
        if (resultsModel != null) {
            loadItemset(resultsModel.resultId)
        }

        with(binding) {
            rvItemset1.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter1
            }
            rvItemset2.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter2
            }
            rvItemset3.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter3
            }
            rvAssoc.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter4
            }
            rvFinal.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = adapter5
            }
        }
    }


    private fun loadItemset(resultId: String) {
         val baseRef = db.collection("users")
            .document("admin")
            .collection("result")
            .document(resultId)
        val i1 = baseRef.collection("itemset1")
        val i2 = baseRef.collection("itemset2")
        val i3 = baseRef.collection("itemset3")
        val i4 = baseRef.collection("assoc_rules")
        val txList: ArrayList<Itemset1> = arrayListOf()

        i1.get().addOnSuccessListener { snapshot1 ->
            val list = snapshot1.mapNotNull { it.toObject(Itemset1::class.java) }
            val txList: MutableList<Itemset1> = arrayListOf()
            for(doc in snapshot1) {
               val item = doc.getString("item")
               val ket = doc.getString("keterangan")
               val sup = doc.getDouble("support")
               val qty = doc.getDouble("totalQuantity")
                val rsl = Itemset1(
                   item,
                   ket,
                   sup,
                   qty
               )
                txList.add(rsl)
                Log.e(TAG, txList.toString())
            }
            adapter1.submitList(txList)
            Log.e(TAG, adapter1.toString())
            i2.get().addOnSuccessListener { snapshot2 ->
                val list2 = snapshot2.mapNotNull { it.toObject(Itemset2::class.java) }
                adapter2.submitList(list2)
                i3.get().addOnSuccessListener { snapshot3 ->
                    val list3  = snapshot3.mapNotNull { it.toObject(Itemset3::class.java) }
                    adapter3.submitList(list3)
                    i4.get().addOnSuccessListener { snapshot4 ->
                        val list4 = snapshot4.mapNotNull { it.toObject(AssociationRule::class.java) }
                        adapter4.submitList(list4)
                        adapter5.submitList(list4)
                        binding.sv.visibility = View.VISIBLE
                        binding.progressBarProcess.visibility = View.GONE
                    }
                }
            }
        }
    }


    companion object {
        const val RESULTID = "resultId"
    }

}
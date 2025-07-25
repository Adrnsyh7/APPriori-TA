package com.submission.appriori.ui.process

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.appriori.R
import com.submission.appriori.ViewModelFactory
import com.submission.appriori.adapter.Itemset1Adapter
import com.submission.appriori.adapter.Itemset1lAdapter
import com.submission.appriori.adapter.Itemset2Adapter
import com.submission.appriori.adapter.Itemset2lAdapter
import com.submission.appriori.adapter.Itemset3Adapter
import com.submission.appriori.adapter.Itemset3lAdapter
import com.submission.appriori.adapter.ItemsetAssocAdapter
import com.submission.appriori.data.ResultState
import com.submission.appriori.data.response.AprioriData
import com.submission.appriori.data.response.Itemset1Item
import com.submission.appriori.databinding.FragmentProcessBinding
import com.submission.appriori.utils.DateConvert
import java.util.Calendar
import java.util.TimeZone


class ProcessFragment : Fragment() {
    private var _binding: FragmentProcessBinding? = null

    private lateinit var itemset1Adapter: Itemset1Adapter
    private lateinit var itemset1lAdapter: Itemset1lAdapter
    private lateinit var itemset2Adapter: Itemset2Adapter
    private lateinit var itemset2lAdapter: Itemset2lAdapter
    private lateinit var itemset3Adapter: Itemset3Adapter
    private lateinit var itemset3lAdapter: Itemset3lAdapter
    private lateinit var itemsetAssocAdapter: ItemsetAssocAdapter
    private lateinit var itemset1Item: Itemset1Item

    private val binding get() = _binding!!
    private var startTimestamp: Long? = null
    private var endTimeStamp: Long? = null
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val viewModel by viewModels<ProcessViewModel> {
        ViewModelFactory.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProcessBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scrollView = activity?.findViewById<ScrollView>(R.id.sv)
        val header = activity?.findViewById<View>(R.id.rl1)
        val header2 =
            activity?.findViewById<View>(R.id.rl2)


        scrollView?.setOnScrollChangeListener { v: View, scrollX: Int, scrollY: Int, _, oldScrollY: Int ->
            if (scrollY != null) {
                if (scrollY > 50 && oldScrollY < scrollY!!) {
                    // Sembunyikan header (animasikan ke atas)
                    header2?.animate()?.translationY(-header?.height?.toFloat()!!)?.setDuration(100)?.start()
                    header2?.setBackgroundResource(R.drawable.card_flat)
                } else if (scrollY < 50 && oldScrollY > scrollY) {
                    // Munculkan kembali header
                    header2?.animate()?.translationY(0f)?.setDuration(100)?.start()
                    header2?.setBackgroundResource(R.drawable.card)
                }
            }
        }


        itemset1Adapter = Itemset1Adapter()
        itemset1lAdapter = Itemset1lAdapter()
        itemset2Adapter = Itemset2Adapter()
        itemset2lAdapter = Itemset2lAdapter()
        itemset3Adapter = Itemset3Adapter()
        itemset3lAdapter = Itemset3lAdapter()
        itemsetAssocAdapter = ItemsetAssocAdapter()
        itemset1Item = Itemset1Item()
        binding.date.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            super.getFragmentManager()?.let { it1 -> datePicker.show(it1, "DatePicker") }

            datePicker.addOnPositiveButtonClickListener { selection ->

                startTimestamp = selection.first
                endTimeStamp = selection.second
                binding.tvDate.text = datePicker.headerText
            }
        }
        binding.btnProcess.setOnClickListener {
            isLoading(true)
            processApriori()
        }

        with(binding) {
            rvItemset1.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset1Adapter
            }
            rvItemset1lolos.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset1lAdapter
            }

            rvItemset2.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset2Adapter
            }
            rvItemset2lolos.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset2lAdapter
            }
            rvItemset3.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset3Adapter
            }
            rvItemset3lolos.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset3lAdapter
            }
            rvAssoc.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemsetAssocAdapter
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun processApriori() {
        val startTimestamp = startTimestamp
        val endTimestamp = endTimeStamp
        if (startTimestamp != null && endTimestamp !=null) {
            val startCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
            startCalendar.timeInMillis = startTimestamp
            startCalendar.set(Calendar.HOUR_OF_DAY, 0)
            startCalendar.set(Calendar.MINUTE, 0 )
            startCalendar.set(Calendar.SECOND, 0)
            startCalendar.set(Calendar.MILLISECOND, 0)
            val startDate = startCalendar.time
            val start = com.google.firebase.Timestamp(startDate)

            val endCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
            endCalendar.timeInMillis = endTimestamp
            endCalendar.set(Calendar.HOUR_OF_DAY, 23)
            endCalendar.set(Calendar.MINUTE, 59)
            endCalendar.set(Calendar.SECOND, 59)
            endCalendar.set(Calendar.MILLISECOND, 999)
            val endDate = endCalendar.time
            val end = com.google.firebase.Timestamp(endDate)
            val support = binding.minSup.text.toString().toDouble()
            val conf = binding.minConf.text.toString().toDouble()
            db.collection("users").document("admin").collection("transactions")
                .whereGreaterThanOrEqualTo("date",start)
                .whereLessThanOrEqualTo("date", end)
                .get()
                .addOnSuccessListener {transactionSnapshot ->
                    val transactionsList = mutableListOf<List<String>>();
                    for(document in transactionSnapshot) {
                        val date = document.getTimestamp("date")?.toDate()
                        val itemRaw = document.get("item")
                        if (date != null && itemRaw != null) {
                            val items: List<String> = when (itemRaw) {
                                is List<*> -> itemRaw.filterIsInstance<String>()
                                is String -> itemRaw.split(",").map { it.trim() }
                                else -> emptyList()
                            }
                            if (items.isNotEmpty()) {
                                transactionsList.add(items)
                            }
                        }

                        Log.d(tag, itemRaw.toString())
                    }

                        val aprioriInput = AprioriData(data = transactionsList, support, conf)
                        Log.d(tag, aprioriInput.toString())
                        if (transactionsList.isNotEmpty()) {
                        viewModel.fetchApriori(aprioriInput).observe(viewLifecycleOwner) {result ->
                            when(result) {
                                is ResultState.Loading -> {
                                    isLoading(true)
                                }
                                is ResultState.Success -> {
                                    Log.e(tag, result.toString())
                                    isLoading(false)
                                    binding.tgl.text = DateConvert.convertDate(startDate) + " - " + DateConvert.convertDate(endDate)
                                    binding.minSupText.text = support.toString()
                                    binding.minConfText.text = conf.toString()
                                    binding.ll0.visibility = View.GONE
                                    binding.sv.visibility = View.VISIBLE
                                    binding.ll00.visibility = View.VISIBLE
                                    itemset1Adapter.submitList(result.data.data?.itemset1)
                                    itemset1lAdapter.submitList(result.data.data?.itemset1Lolos)
                                    itemset2Adapter.submitList(result.data.data?.itemset2)
                                    itemset2lAdapter.submitList(result.data.data?.itemset2Lolos)
                                    itemset3Adapter.submitList(result.data.data?.itemset3)
                                    itemset3lAdapter.submitList(result.data.data?.itemset3Lolos)
                                    itemsetAssocAdapter.submitList(result.data.data?.associationRules)
                                    val timestamp = System.currentTimeMillis()
                                    val resultId = "result_${timestamp}" // dokumen unik berdasarkan waktu
                                    val aprioriData = result.data.data ?: return@observe
                                    val meta = hashMapOf(
                                        "created_at" to FieldValue.serverTimestamp(),
                                        "from" to startDate,
                                        "end" to endDate,
                                        "min_support" to support,
                                        "conf" to conf,
                                    )
                                    db.collection("users").document("admin").collection("result").document(resultId).set(meta)
                                    aprioriData.itemset1?.forEach {
                                        if (it != null) {
                                            db.collection("users").document("admin").collection("result").document(resultId).collection("itemset1").add(it)
                                        }
                                    }
                                    aprioriData.itemset2?.forEach {
                                        if (it != null) {
                                            db.collection("users").document("admin").collection("result").document(resultId).collection("itemset2").add(it)
                                        }
                                    }
                                    aprioriData.itemset2Lolos?.forEach {
                                        if (it != null) {
                                            db.collection("users").document("admin").collection("result").document(resultId).collection("itemset2lolos").add(it)
                                        }
                                    }
                                    aprioriData.itemset3?.forEach {
                                        if (it != null) {
                                            db.collection("users").document("admin").collection("result").document(resultId).collection("itemset3").add(it)
                                        }
                                    }
                                    aprioriData.itemset3Lolos?.forEach {
                                        if (it != null) {
                                            db.collection("users").document("admin").collection("result").document(resultId).collection("itemset3l").add(it)
                                        }
                                    }
                                    aprioriData.associationRules?.forEach {
                                        if (it != null) {
                                            db.collection("users").document("admin").collection("result").document(resultId).collection("assoc_rules").add(it)
                                        }
                                    }

                                }
                                is ResultState.Error -> {
                                    isLoading(false)
                                }
                            }

                        }
                        } else {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Pesan Kesalahan")
                                .setMessage("Data tidak ditemukan pada rentang waktu tersebut.")
                                .setNeutralButton("OK", null)
                                .show()
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error fetching transactions: ", e)
                }
        }
    }


    private fun isLoading(isLoading: Boolean) {
        binding.progressBarProcess.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}
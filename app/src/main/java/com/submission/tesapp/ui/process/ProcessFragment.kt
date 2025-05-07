package com.submission.tesapp.ui.process

import android.content.ClipData.Item
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.submission.tesapp.R
import com.submission.tesapp.ViewModelFactory
import com.submission.tesapp.adapter.Itemset1Adapter
import com.submission.tesapp.adapter.Itemset1lAdapter
import com.submission.tesapp.adapter.Itemset2Adapter
import com.submission.tesapp.adapter.Itemset2lAdapter
import com.submission.tesapp.adapter.Itemset3Adapter
import com.submission.tesapp.adapter.Itemset3lAdapter
import com.submission.tesapp.data.ResultState
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.data.response.AprioriData
import com.submission.tesapp.data.response.Data
import com.submission.tesapp.databinding.FragmentProcessBinding
import java.sql.Timestamp
import java.util.Calendar
import java.util.TimeZone

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProcessFragment : Fragment() {
    private var _binding: FragmentProcessBinding? = null

    private lateinit var itemset1Adapter: Itemset1Adapter
    private lateinit var itemset1lAdapter: Itemset1lAdapter
    private lateinit var itemset2Adapter: Itemset2Adapter
    private lateinit var itemset2lAdapter: Itemset2lAdapter
    private lateinit var itemset3Adapter: Itemset3Adapter
    private lateinit var itemset3lAdapter: Itemset3lAdapter

    private val binding get() = _binding!!
    private var startTimestamp: Long? = null
    private var endTimeStamp: Long? = null
    private val db = FirebaseFirestore.getInstance()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemset1Adapter = Itemset1Adapter()
        itemset1lAdapter = Itemset1lAdapter()
        itemset2Adapter = Itemset2Adapter()
        itemset2lAdapter = Itemset2lAdapter()
        itemset3Adapter = Itemset3Adapter()
        itemset3lAdapter = Itemset3lAdapter()
        binding.date.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            super.getFragmentManager()?.let { it1 -> datePicker.show(it1, "DatePicker") }
            datePicker.addOnPositiveButtonClickListener { selection ->
                startTimestamp = selection.first
                endTimeStamp = selection.second
                binding.addTvDueDate.text = datePicker.headerText
            }
        }
        binding.btnProcess.setOnClickListener {
            processApriori()
        }
    }

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
            endCalendar.timeInMillis = startTimestamp
            endCalendar.set(Calendar.HOUR_OF_DAY, 23)
            endCalendar.set(Calendar.MINUTE, 59)
            endCalendar.set(Calendar.SECOND, 59)
            endCalendar.set(Calendar.MILLISECOND, 999)
            val endDate = endCalendar.time
            val end = com.google.firebase.Timestamp(endDate)

            db.collection("transactions")
                .whereGreaterThan("date",start)
                .whereLessThan("date",end)
                .get()
                .addOnSuccessListener {transactionSnapshot ->
                    val transactionsByDate = mutableMapOf<String, MutableList<String>>()
                    for(document in transactionSnapshot) {
                        val date = document.getTimestamp("date")?.toDate()?.toString()
                        val item = document.getString("obat")
                        val support = binding.minSup.text.toString().toDouble()
                        val conf = binding.minConf.text.toString().toDouble()
                        if (date != null && item != null) {
                            transactionsByDate.getOrPut(date) { mutableListOf() }.add(item)
                        }
                        val aprioriInput = AprioriData(transactionsByDate, support, conf)
                        viewModel.fetchApriori(aprioriInput).observe(viewLifecycleOwner) {result ->
                            when(result) {
                                is ResultState.Loading -> {

                                }
                                is ResultState.Success -> {
                                    itemset1Adapter.submitList(result.data.data?.itemset1)
                                    itemset1lAdapter.submitList(result.data.data?.itemset1Lolos)
                                    itemset2Adapter.submitList(result.data.data?.itemset2)
                                    itemset2lAdapter.submitList(result.data.data?.itemset2Lolos)
                                    itemset3Adapter.submitList(result.data.data?.itemset3)
                                    itemset3lAdapter.submitList(result.data.data?.itemset3Lolos)
                                    val intent = Intent(context, ResultFragment::class.java)
                                    startActivity(intent)
                                }
                                is ResultState.Error -> {

                                }
                            }

                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error fetching transactions: ", e)
                }


        }

    }
}
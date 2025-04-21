package com.submission.tesapp.ui.process

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.tesapp.R
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
    private val binding get() = _binding!!
    private var startTimestamp: Long? = null
    private var endTimeStamp: Long? = null
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProcessBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.date.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
            super.getFragmentManager()?.let { it1 -> datePicker.show(it1, "DatePicker") }
            datePicker.addOnPositiveButtonClickListener { selection ->
                startTimestamp = selection.first
                endTimeStamp = selection.second
                binding.addTvDueDate.text = datePicker.headerText
            }
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


        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProcessFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProcessFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
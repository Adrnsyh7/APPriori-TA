package com.submission.tesapp.ui.transactions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.submission.tesapp.R
import com.submission.tesapp.databinding.FragmentInputBinding
import com.submission.tesapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class InputTransactionFragment : Fragment(), DatePickerFragment.DialogDateListener {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnInput.setOnClickListener {
            saveTransactions()
        }




    }

    private fun saveTransactions() {
        val item = binding.addDrug.text.toString()
        val dateText = binding.addDate.text.toString()
        val date = dueDateMillis

    }
    companion object {

        var dueDateMillis: Long = System.currentTimeMillis()
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InputTransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
binding.addDate.text = dateFormat.format(calendar.time)

        dueDateMillis = dateFormat.calendar.timeInMillis
    }

}
package com.submission.tesapp.ui.transactions

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.submission.tesapp.MainActivity
import com.submission.tesapp.R
import com.submission.tesapp.databinding.FragmentInputBinding
import com.submission.tesapp.firebase.FirebaseDataManager
import com.submission.tesapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class InputTransactionFragment : Fragment(){

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!
    private val firebaseDataManager: FirebaseDataManager = FirebaseDataManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.date.setOnClickListener {
            val dialogFragment = DatePickerFragment()
            dialogFragment.show(requireActivity().supportFragmentManager, "datePicker")
        }

        binding.btnInput.setOnClickListener {
            saveTransactions()
        }
    }

    private fun saveTransactions() {
        val item = binding.addDrug.text.toString()
        val dateText = binding.addDate.text.toString()
        val date = dueDateMillis

        firebaseDataManager.saveTransaction(item, date) {success ->
            if(success) {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            } else {

            }

        }

    }
    companion object {

        var dueDateMillis: Long = System.currentTimeMillis()
        private const val TRANSACTION_ID = "TRANSACTION_ID"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(transactionId: String) : InputTransactionFragment {
            val fragment = InputTransactionFragment()
            val args = Bundle()
            args.putString(TRANSACTION_ID, transactionId)
            fragment.arguments = args
            return fragment
        }
    }


}
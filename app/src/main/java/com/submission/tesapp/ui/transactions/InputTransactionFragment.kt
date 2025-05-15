package com.submission.tesapp.ui.transactions

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.ui.MainActivity
import com.submission.tesapp.databinding.FragmentInputBinding
import com.submission.tesapp.firebase.FirebaseDataManager
import com.submission.tesapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Date
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

        editTransactions()
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
    private fun editTransactions() {
        val txId: TransactionModel? = requireActivity().intent.getParcelableExtra(ID)
        if(txId != null) {
            binding.addDrug.setText(txId?.item)
            val item = binding.addDrug.text.toString()
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val formattedTimestamp = dateFormat.format(txId?.date ?: Date())
            binding.addDate.setText(formattedTimestamp)
            binding.date.setOnClickListener {
                val dialogFragment = DatePickerFragment()
                dialogFragment.show(requireActivity().supportFragmentManager, "datePicker")
            }

            firebaseDataManager.updateTransactions(ID, item, dueDateMillis) { success ->
                if(success) {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    binding.addDrug.text?.clear()

                } else {

                }
            }
        }

    }


    companion object {

        var dueDateMillis: Long = System.currentTimeMillis()
        const val ID = "id"
    }


}
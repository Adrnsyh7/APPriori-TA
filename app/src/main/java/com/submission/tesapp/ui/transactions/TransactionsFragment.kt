package com.submission.tesapp.ui.transactions

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.tesapp.R
import com.submission.tesapp.adapter.TransactionAdapter
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.databinding.FragmentTransactionsBinding
import com.submission.tesapp.firebase.FirebaseDataManager

class TransactionsFragment : Fragment() {
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TransactionAdapter
    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarProcess.visibility = View.VISIBLE
        binding.ll2.visibility = View.INVISIBLE
        adapter = TransactionAdapter()
        firebaseDataManager = FirebaseDataManager()
        val layoutManager = LinearLayoutManager(requireParentFragment().requireContext())
        binding.rvTx.layoutManager = layoutManager
        setupFirebase()
        binding.rvTx.setOnClickListener {
            val intent: TransactionModel? = requireActivity().intent.getParcelableExtra(ID)
            intent?.id
            Log.e(TAG, intent?.id.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupFirebase() {
        firebaseDataManager.getTransactions { list ->
            binding.rvTx.setHasFixedSize(true)
            binding.rvTx.adapter = adapter
            binding.progressBarProcess.visibility = View.GONE
            binding.ll2.visibility = View.VISIBLE
            adapter.submitList(list)
            if(binding.rvTx.visibility == View.VISIBLE) {
                adapter.submitList(list)
            }
        }
    }

 companion object {
     const val ID = "id"
 }
}
package com.submission.tesapp.ui.report

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.tesapp.adapter.ReportAdapter
import com.submission.tesapp.databinding.FragmentReportBinding
import com.submission.tesapp.firebase.FirebaseDataManager


class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReportAdapter
    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ReportAdapter()
        firebaseDataManager = FirebaseDataManager()
        val layoutManager = LinearLayoutManager(requireParentFragment().requireContext())
        binding.rvReport.layoutManager = layoutManager
        setupFirebase()
    }

    private fun setupFirebase() {
        firebaseDataManager.getResult { list ->
            Log.e(tag, list.toString())
            binding.rvReport.setHasFixedSize(true)
            binding.rvReport.adapter = adapter
            adapter.submitList(list)
        }
    }

    companion object {

    }
}
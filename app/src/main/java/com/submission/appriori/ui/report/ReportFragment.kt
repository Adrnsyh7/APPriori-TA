package com.submission.appriori.ui.report

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.appriori.adapter.ReportAdapter
import com.submission.appriori.databinding.FragmentReportBinding
import com.submission.appriori.firebase.FirebaseDataManager


class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReportAdapter
    private lateinit var firebaseDataManager: FirebaseDataManager
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cv1.visibility = View.INVISIBLE
        binding.progressBarProcess.visibility = View.VISIBLE
        adapter = ReportAdapter()
        firebaseDataManager = FirebaseDataManager()
        val layoutManager = LinearLayoutManager(requireParentFragment().requireContext())
        binding.rvReport.layoutManager = layoutManager
        setupFirebase()
        binding.deleteAll.setOnClickListener {
            showDeleteConfirmationDialog()
        }

    }

    private fun setupFirebase() {

        db.collection("users").document("admin").collection("result").get()

            .addOnSuccessListener { snapshot ->
                Log.i(tag, snapshot.isEmpty.toString())
                //binding.progressBarProcess.visibility = View.GONE
                if (snapshot.isEmpty) {
                    binding.progressBarProcess.visibility = View.GONE
                    binding.noData.visibility = View.VISIBLE
                } else {
                    firebaseDataManager.getResult { list ->
                        Log.e(tag, list.isEmpty().toString())
                        binding.rvReport.setHasFixedSize(true)
                        binding.rvReport.adapter = adapter
                        binding.cv1.visibility = View.VISIBLE
                        binding.progressBarProcess.visibility = View.GONE
                        adapter.submitList(list)
                    }
                }
                for (document in snapshot) {
                    val data = document.data
                    val id = document.id
                    Log.i(tag, id.toString())
                    val created_at = data["created_at"] as? Timestamp
                    if (created_at.toString().isEmpty()) {
                       // binding.progressBarProcess.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    } else {
                        firebaseDataManager.getResult { list ->
                            Log.e(tag, list.isEmpty().toString())
                            binding.rvReport.setHasFixedSize(true)
                            binding.rvReport.adapter = adapter
                            binding.cv1.visibility = View.VISIBLE
                            binding.progressBarProcess.visibility = View.GONE
                            adapter.submitList(list)
                        }
                    }
                }
            }
//        firebaseDataManager.getResult { list ->
//            Log.e(tag, list.isEmpty().toString())
//            binding.rvReport.setHasFixedSize(true)
//            binding.rvReport.adapter = adapter
//            binding.cv1.visibility = View.VISIBLE
//            binding.progressBarProcess.visibility = View.GONE

//            db.collection("users")
//                .document("admin")
//                .collection("result").get()
//                .addOnSuccessListener {snapshot ->
//                    for(document in snapshot) {
//                        val data = document.data
//        if (list.isEmpty()) {
//            Log.e(tag, "kosong")
//            binding.progressBarProcess.visibility = View.GONE
//            binding.cv1.visibility = View.INVISIBLE
//            binding.noData.visibility = View.VISIBLE
//        } else {
//            Log.e(tag, "isi")
//            binding.progressBarProcess.visibility = View.GONE
//            binding.cv1.visibility = View.VISIBLE
//            adapter.submitList(list)
            //  }
//                    }
//
//                }

//            }

            // }
        }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi")
            .setMessage("Apakah kamu yakin akan menghapus semua transaksi?")
            .setPositiveButton("Hapus") { _, _ ->
                binding.cv1.visibility = View.INVISIBLE
//                binding.progressBarProcess.visibility = View.VISIBLE
                deleteResult()
            }.setNegativeButton("Batal", null)
            .show()
    }

    private fun deleteResult() {
        firebaseDataManager.deleteAllResult { deleteSuccess ->
            if (deleteSuccess) {
                binding.progressBarProcess.visibility = View.GONE
                setupFirebase()
                showToast("Data hasil berhasil dihapus")
            } else {
                binding.rvReport.visibility = View.VISIBLE
                binding.progressBarProcess.visibility = View.GONE
                showToast("Gagal menghapus data hasil")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

    }
}
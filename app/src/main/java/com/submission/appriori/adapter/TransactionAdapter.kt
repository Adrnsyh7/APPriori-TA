package com.submission.appriori.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.submission.appriori.R

import com.submission.appriori.data.model.TransactionModel
import com.submission.appriori.databinding.FragmentTransactionsBinding
import com.submission.appriori.databinding.ItemTransactionsBinding
import com.submission.appriori.firebase.FirebaseDataManager
import com.submission.appriori.ui.transactions.InputTransactionsActivity
import com.submission.appriori.utils.DateConvert

class TransactionAdapter: ListAdapter<TransactionModel, TransactionAdapter.ViewHolder>(DIFF_CALLBACK) {
   class ViewHolder(private val binding: ItemTransactionsBinding, private val context: Context, private val binding1: FragmentTransactionsBinding) : RecyclerView.ViewHolder(binding.root)
   {
       private val db: FirebaseDataManager = FirebaseDataManager()
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(userData: TransactionModel, position: Int) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem.text = userData.item
                tvDate.text = DateConvert.convertDate(userData.date)
                gambar.setOnClickListener {
                        val intent = Intent(context, InputTransactionsActivity::class.java)
                        intent.putExtra(InputTransactionsActivity.ID, userData)
                        context.startActivity(intent)
                    if (context is Activity) {
                        context.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
                    }
                }
                gambar2.setOnClickListener {
                        userData?.id?.let { it1 -> showDeleteConfirmationDialog(it1) }
                }

            }
        }

       private fun showDeleteConfirmationDialog(id: String) {
           AlertDialog.Builder(context)
               .setTitle("Konfirmasi")
               .setMessage("Apakah kamu yakin akan menghapus transaksi ini?")
               .setPositiveButton("Hapus") { _, _ ->
                   deleteTransaction(id)
                   binding1.progressBarProcess.visibility = View.VISIBLE
                   binding1.rvTx.visibility = View.INVISIBLE

               }.setNegativeButton("Batal", null)
                .show()
       }


       private fun deleteTransaction(id: String) {
           db.deleteTransactions(id) { deleteSuccess ->
                   if (deleteSuccess) {
                       binding1.rvTx.visibility = View.VISIBLE
                       binding1.progressBarProcess.visibility = View.GONE
                       showToast("Transaction deleted successfully")
                   } else {
                       binding1.rvTx.visibility = View.VISIBLE
                       binding1.progressBarProcess.visibility = View.GONE
                       showToast("Failed to delete transaction")
                   }
               }
            }
       private fun showToast(message: String) {
           Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
       }
       }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionModel>()  {
            override fun areItemsTheSame(
                oldItem: TransactionModel,
                newItem: TransactionModel
            ): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(
                oldItem: TransactionModel,
                newItem: TransactionModel
            ): Boolean {
                return oldItem == newItem
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val binding1 = FragmentTransactionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionAdapter.ViewHolder(binding, parent.context, binding1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem, position)
    }

}

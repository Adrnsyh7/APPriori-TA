package com.submission.tesapp.adapter

import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.R
import com.google.firebase.firestore.FirebaseFirestore

import com.submission.tesapp.data.model.AssociationRule
import com.submission.tesapp.data.model.Itemset1
import com.submission.tesapp.data.model.Itemset2
import com.submission.tesapp.data.model.Itemset3
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.data.response.Itemset3Item
import com.submission.tesapp.databinding.FragmentTransactionsBinding
import com.submission.tesapp.databinding.ItemResultAssocBinding
import com.submission.tesapp.databinding.ItemResultBinding
import com.submission.tesapp.databinding.ItemResultItemset1LBinding
import com.submission.tesapp.databinding.ItemResultItemset2Binding
import com.submission.tesapp.databinding.ItemResultItemset2LBinding
import com.submission.tesapp.databinding.ItemResultItemset3Binding
import com.submission.tesapp.databinding.ItemTransactionsBinding
import com.submission.tesapp.firebase.FirebaseDataManager
import com.submission.tesapp.ui.MainActivity
import com.submission.tesapp.ui.transactions.InputTransactionFragment
import com.submission.tesapp.ui.transactions.InputTransactionsActivity
import com.submission.tesapp.ui.transactions.TransactionsFragment
import com.submission.tesapp.utils.DateConvert
import okhttp3.internal.notify

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

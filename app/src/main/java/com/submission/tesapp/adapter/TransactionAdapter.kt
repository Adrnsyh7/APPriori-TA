package com.submission.tesapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.databinding.ItemTransactionsBinding
import com.submission.tesapp.utils.DateConvert

class TransactionAdapter: ListAdapter<TransactionModel, TransactionAdapter.ViewHolder>(DIFF_CALLBACK) {
   class ViewHolder(private val binding: ItemTransactionsBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(userData: TransactionModel, position: Int) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem.text = userData.item
                tvDate.text = DateConvert.convertDate(userData.date)
            }
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
        return TransactionAdapter.ViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem, position)
    }
}
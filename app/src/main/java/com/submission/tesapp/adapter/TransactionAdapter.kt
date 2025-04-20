package com.submission.tesapp.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.submission.tesapp.data.model.TransactionModel

class TransactionAdapter(private val context: Context) : ListAdapter<TransactionModel, TransactionAdapter.MyViewHolder>(DIFF_CALLBACK) {
    inner class MyViewHolder {

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionModel>()  {
            override fun areItemsTheSame(
                oldItem: TransactionModel,
                newItem: TransactionModel
            ): Boolean {
                return oldItem.id == newItem.id

            }

            override fun areContentsTheSame(
                oldItem: TransactionModel,
                newItem: TransactionModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}
package com.submission.tesapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.tesapp.adapter.Itemset11Adapter.ViewHolder.Companion.DIFF_CALLBACK11
import com.submission.tesapp.adapter.Itemset1Adapter.ViewHolder.Companion.DIFF_CALLBACK1
import com.submission.tesapp.adapter.Itemset21Adapter.ViewHolder.Companion.DIFF_CALLBACK12
import com.submission.tesapp.adapter.Itemset31Adapter.ViewHolder.Companion.DIFF_CALLBACK13
import com.submission.tesapp.adapter.ItemsetAssoc1Adapter.ViewHolder.Companion.DIFF_CALLBACK14
import com.submission.tesapp.data.model.AssociationRule
import com.submission.tesapp.data.model.Itemset1
import com.submission.tesapp.data.model.Itemset2
import com.submission.tesapp.data.model.Itemset3
import com.submission.tesapp.data.model.TransactionModel
import com.submission.tesapp.data.response.Itemset3Item
import com.submission.tesapp.databinding.ItemResultAssocBinding
import com.submission.tesapp.databinding.ItemResultBinding
import com.submission.tesapp.databinding.ItemResultItemset1LBinding
import com.submission.tesapp.databinding.ItemResultItemset2Binding
import com.submission.tesapp.databinding.ItemResultItemset2LBinding
import com.submission.tesapp.databinding.ItemResultItemset3Binding
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

class Itemset11Adapter : ListAdapter<Itemset1, Itemset11Adapter.ViewHolder>(DIFF_CALLBACK11) {
    class ViewHolder(
        private var binding: ItemResultBinding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset1: Itemset1, position: Int) {
            binding.tvNo.text = (position + 1).toString()
            binding.tvItem.text = itemset1.item?.toString()
            binding.tvJumlah.text = itemset1.totalQuantity.toString()
            binding.tvSupport.text = itemset1.support.toString()
            binding.tvKeterangan.text = itemset1.keterangan

        }

        companion object {
            val DIFF_CALLBACK11 = object : DiffUtil.ItemCallback<Itemset1>() {
                override fun areItemsTheSame(
                    oldItem: Itemset1,
                    newItem: Itemset1
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset1,
                    newItem: Itemset1
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem, position)
    }
}

class Itemset21Adapter : ListAdapter<Itemset2, Itemset21Adapter.ViewHolder>(DIFF_CALLBACK12) {
    class ViewHolder(
        private var binding: ItemResultItemset2Binding,
        private val context: Context,
        private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    ) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(itemset2: Itemset2, position: Int) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem1.text = itemset2.item1
                tvItem2.text = itemset2.item2
                tvSupport.text= itemset2.support.toString()
                tvJumlah.text = itemset2.count.toString()
            }
        }



        companion object {
            val DIFF_CALLBACK12 = object : DiffUtil.ItemCallback<Itemset2>() {
                override fun areItemsTheSame(
                    oldItem: Itemset2,
                    newItem: Itemset2
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset2,
                    newItem: Itemset2
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultItemset2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Itemset21Adapter.ViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem, position)

    }
}

class Itemset31Adapter : ListAdapter<Itemset3, Itemset31Adapter.ViewHolder>(DIFF_CALLBACK13) {
    class ViewHolder(
        private var binding: ItemResultItemset3Binding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(Itemset3: Itemset3) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem1.text = Itemset3.item1.toString()
                tvItem2.text = Itemset3.item2.toString()
                tvItem3.text = Itemset3.item3.toString()
                tvSupport.text = Itemset3.support.toString()
                tvKeterangan.text = Itemset3.keterangan.toString()
                tvJumlah.text = Itemset3.count.toString()
            }
        }



        companion object {
            val DIFF_CALLBACK13 = object : DiffUtil.ItemCallback<Itemset3>() {
                override fun areItemsTheSame(
                    oldItem: Itemset3,
                    newItem: Itemset3
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset3,
                    newItem: Itemset3
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Itemset31Adapter.ViewHolder {
        val binding = ItemResultItemset3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Itemset31Adapter.ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: Itemset31Adapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}

class ItemsetAssoc1Adapter : ListAdapter<AssociationRule, ItemsetAssoc1Adapter.ViewHolder>(DIFF_CALLBACK14) {
    class ViewHolder(
        private var binding: ItemResultAssocBinding,
        private val context: Context,

        ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemsetassoc: AssociationRule) {
            with(binding) {
                with(binding) {
                    tvNo.text = (position + 1).toString()
                    tvItem.text = itemsetassoc.antecedents.joinToString { " " } + " => " + itemsetassoc.consequents.joinToString { " " }
                    tvLift.text = itemsetassoc.support.toString()
                    tvConf.text = itemsetassoc.confidence.toString()
                }
            }
        }



        companion object {
            val DIFF_CALLBACK14 = object : DiffUtil.ItemCallback<AssociationRule>() {
                override fun areItemsTheSame(
                    oldItem: AssociationRule,
                    newItem: AssociationRule
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: AssociationRule,
                    newItem: AssociationRule
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ItemsetAssoc1Adapter.ViewHolder {
        val binding = ItemResultAssocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsetAssoc1Adapter.ViewHolder(binding, parent.context)
    }


    override fun onBindViewHolder(holder: ItemsetAssoc1Adapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}
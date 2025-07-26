package com.submission.appriori.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.appriori.adapter.Itemset1Adapter.ViewHolder.Companion.DIFF_CALLBACK1
import com.submission.appriori.adapter.Itemset1lAdapter.ViewHolder.Companion.DIFF_CALLBACK
import com.submission.appriori.adapter.Itemset2Adapter.ViewHolder.Companion.DIFF_CALLBACK3
import com.submission.appriori.adapter.Itemset2lAdapter.ViewHolder.Companion.DIFF_CALLBACK4
import com.submission.appriori.adapter.Itemset3Adapter.ViewHolder.Companion.DIFF_CALLBACK5
import com.submission.appriori.adapter.Itemset3lAdapter.ViewHolder.Companion.DIFF_CALLBACK6
import com.submission.appriori.adapter.ItemsetAssocAdapter.ViewHolder.Companion.DIFF_CALLBACK7
import com.submission.appriori.data.response.AssociationRulesItem
import com.submission.appriori.data.response.Itemset1Item
import com.submission.appriori.data.response.Itemset1LolosItem
import com.submission.appriori.data.response.Itemset2Item
import com.submission.appriori.data.response.Itemset2LolosItem
import com.submission.appriori.data.response.Itemset3Item
import com.submission.appriori.data.response.Itemset3LolosItem
import com.submission.appriori.databinding.ItemResultAssocBinding
import com.submission.appriori.databinding.ItemResultBinding
import com.submission.appriori.databinding.ItemResultItemset1LBinding
import com.submission.appriori.databinding.ItemResultItemset2Binding
import com.submission.appriori.databinding.ItemResultItemset2LBinding
import com.submission.appriori.databinding.ItemResultItemset3Binding
import com.submission.appriori.databinding.ItemResultItemset3LBinding

class Itemset1Adapter : ListAdapter<Itemset1Item, Itemset1Adapter.ViewHolder>(DIFF_CALLBACK1) {
    class ViewHolder(
        private var binding: ItemResultBinding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(itemset1: Itemset1Item, position: Int) {
            binding.tvNo.text = (position + 1).toString()
            binding.tvItem.text = itemset1.item?.toString()
            binding.tvJumlah.text = itemset1.totalQuantity.toString()
            binding.tvSupport.text = (itemset1.support?.let { Math.round(it.times(100)) }
                ?.div(100.0)).toString()
            binding.tvKeterangan.text = itemset1.keterangan

        }

        companion object {
            val DIFF_CALLBACK1 = object : DiffUtil.ItemCallback<Itemset1Item>() {
                override fun areItemsTheSame(
                    oldItem: Itemset1Item,
                    newItem: Itemset1Item
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset1Item,
                    newItem: Itemset1Item
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

class Itemset1lAdapter : ListAdapter<Itemset1LolosItem, Itemset1lAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(
        private var binding: ItemResultItemset1LBinding,
        private val context: Context,
        private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    ) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(itemset1l: Itemset1LolosItem, position: Int) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem.text = itemset1l.item
                tvSupport.text= (itemset1l.support?.let { Math.round(it.times(100)) }
                    ?.div(100.0)).toString()
                tvJumlah.text = itemset1l.totalQuantity.toString()
            }
        }



        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Itemset1LolosItem>() {
                override fun areItemsTheSame(
                    oldItem: Itemset1LolosItem,
                    newItem: Itemset1LolosItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset1LolosItem,
                    newItem: Itemset1LolosItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultItemset1LBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Itemset1lAdapter.ViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem, position)

    }
}

class Itemset2Adapter : ListAdapter<Itemset2Item, Itemset2Adapter.ViewHolder>(DIFF_CALLBACK3) {
    class ViewHolder(
        private var binding: ItemResultItemset2Binding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset2: Itemset2Item) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem1.text = itemset2.itemsets1.toString()
                tvItem2.text = itemset2.itemsets2.toString()
                tvSupport.text = (itemset2.support?.let { Math.round(it.times(100)) }
                    ?.div(100.0)).toString()
                tvKeterangan.text = itemset2.keterangan.toString()
                tvJumlah.text = itemset2.count.toString()
            }
        }



        companion object {
            val DIFF_CALLBACK3 = object : DiffUtil.ItemCallback<Itemset2Item>() {
                override fun areItemsTheSame(
                    oldItem: Itemset2Item,
                    newItem: Itemset2Item
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset2Item,
                    newItem: Itemset2Item
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Itemset2Adapter.ViewHolder {
        val binding = ItemResultItemset2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Itemset2Adapter.ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: Itemset2Adapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}

class Itemset2lAdapter : ListAdapter<Itemset2LolosItem, Itemset2lAdapter.ViewHolder>(DIFF_CALLBACK4) {
    class ViewHolder(
        private var binding: ItemResultItemset2LBinding,
        private val context: Context,

    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset2l: Itemset2LolosItem) {
            with(binding) {
                with(binding) {
                    tvNo.text = (position + 1).toString()
                    tvItem1.text = itemset2l.itemsets1.toString()
                    tvItem2.text = itemset2l.itemsets2.toString()
                    tvSupport.text = (itemset2l.support?.let { Math.round(it.times(100)) }
                        ?.div(100.0)).toString()
                    tvJumlah.text = itemset2l.count.toString()
                }
            }
        }



        companion object {
            val DIFF_CALLBACK4 = object : DiffUtil.ItemCallback<Itemset2LolosItem>() {
                override fun areItemsTheSame(
                    oldItem: Itemset2LolosItem,
                    newItem: Itemset2LolosItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset2LolosItem,
                    newItem: Itemset2LolosItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Itemset2lAdapter.ViewHolder {
        val binding = ItemResultItemset2LBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Itemset2lAdapter.ViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Itemset2lAdapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}

class Itemset3Adapter : ListAdapter<Itemset3Item, Itemset3Adapter.ViewHolder>(DIFF_CALLBACK5) {
    class ViewHolder(
        private var binding: ItemResultItemset3Binding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset3: Itemset3Item) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem1.text = itemset3.itemsets1
                tvItem2.text = itemset3.itemsets2
                tvItem3.text = itemset3.itemsets3
                tvSupport.text = (itemset3.support?.let { Math.round(it.times(100)) }
                    ?.div(100.0)).toString()
                tvKeterangan.text = itemset3.keterangan.toString()
                tvJumlah.text = itemset3.count.toString()
            }
        }



        companion object {
            val DIFF_CALLBACK5 = object : DiffUtil.ItemCallback<Itemset3Item>() {
                override fun areItemsTheSame(
                    oldItem: Itemset3Item,
                    newItem: Itemset3Item
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset3Item,
                    newItem: Itemset3Item
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Itemset3Adapter.ViewHolder {
        val binding = ItemResultItemset3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Itemset3Adapter.ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: Itemset3Adapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}

class Itemset3lAdapter : ListAdapter<Itemset3LolosItem, Itemset3lAdapter.ViewHolder>(DIFF_CALLBACK6) {
    class ViewHolder(
        private var binding: ItemResultItemset3LBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset3l: Itemset3LolosItem) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem1.text = itemset3l.itemsets1
                tvItem2.text = itemset3l.itemsets2
                tvItem3.text = itemset3l.itemsets3
                tvSupport.text = (itemset3l.support?.let { Math.round(it.times(100)) }
                    ?.div(100.0)).toString()
                tvJumlah.text = itemset3l.count.toString()
            }
        }

        companion object {
            val DIFF_CALLBACK6 = object : DiffUtil.ItemCallback<Itemset3LolosItem>() {
                override fun areItemsTheSame(
                    oldItem: Itemset3LolosItem,
                    newItem: Itemset3LolosItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: Itemset3LolosItem,
                    newItem: Itemset3LolosItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Itemset3lAdapter.ViewHolder {
        val binding = ItemResultItemset3LBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Itemset3lAdapter.ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: Itemset3lAdapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}

class ItemsetAssocAdapter : ListAdapter<AssociationRulesItem, ItemsetAssocAdapter.ViewHolder>(DIFF_CALLBACK7) {
    class ViewHolder(
        private var binding: ItemResultAssocBinding,
        private val context: Context,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(assoc: AssociationRulesItem) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem.text = assoc.antecedents.toString() + " => " + assoc.consequents.toString()
                tvConf.text = (assoc.confidence?.let { Math.round(it.times(100)) }
                    ?.div(100.0)).toString()
                tvLift.text = (assoc.lift?.let { Math.round(it.times(100)) }
                    ?.div(100.0)).toString()
                if(assoc.lift!! >= 1) {
                    tvKeterangan.text = "Korelasi Positif"
                } else {
                    tvKeterangan.text = "Korelasi Negatif"
                }
            }
        }



        companion object {
            val DIFF_CALLBACK7 = object : DiffUtil.ItemCallback<AssociationRulesItem>() {
                override fun areItemsTheSame(
                    oldItem: AssociationRulesItem,
                    newItem: AssociationRulesItem
                ): Boolean {
                    return oldItem.antecedents == newItem.antecedents
                }

                override fun areContentsTheSame(
                    oldItem: AssociationRulesItem,
                    newItem: AssociationRulesItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsetAssocAdapter.ViewHolder {
        val binding = ItemResultAssocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsetAssocAdapter.ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ItemsetAssocAdapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}
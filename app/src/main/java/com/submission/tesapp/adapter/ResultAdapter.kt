package com.submission.tesapp.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.submission.tesapp.adapter.Itemset1Adapter.ViewHolder.Companion.DIFF_CALLBACK1
import com.submission.tesapp.adapter.Itemset1lAdapter.ViewHolder.Companion.DIFF_CALLBACK
import com.submission.tesapp.adapter.Itemset2Adapter.ViewHolder.Companion.DIFF_CALLBACK3
import com.submission.tesapp.adapter.Itemset2lAdapter.ViewHolder.Companion.DIFF_CALLBACK4
import com.submission.tesapp.adapter.Itemset3Adapter.ViewHolder.Companion.DIFF_CALLBACK5
import com.submission.tesapp.adapter.Itemset3lAdapter.ViewHolder.Companion.DIFF_CALLBACK6
import com.submission.tesapp.data.response.Itemset1Item
import com.submission.tesapp.data.response.Itemset1LolosItem
import com.submission.tesapp.data.response.Itemset2Item
import com.submission.tesapp.data.response.Itemset2LolosItem
import com.submission.tesapp.data.response.Itemset3Item
import com.submission.tesapp.data.response.Itemset3LolosItem
import com.submission.tesapp.databinding.FragmentResultBinding
import com.submission.tesapp.databinding.ItemResultBinding
import com.submission.tesapp.databinding.ItemResultItemset1LBinding
import com.submission.tesapp.databinding.ItemResultItemset2Binding
import com.submission.tesapp.databinding.ItemResultItemset2LBinding
import com.submission.tesapp.databinding.ItemResultItemset3Binding
import com.submission.tesapp.databinding.ItemResultItemset3LBinding
import com.submission.tesapp.databinding.ItemTransactionsBinding
import com.submission.tesapp.ui.process.ResultFragment

class Itemset1Adapter : ListAdapter<Itemset1Item, Itemset1Adapter.ViewHolder>(DIFF_CALLBACK1) {
    class ViewHolder(
        private var binding: ItemResultBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset1: Itemset1Item) {
            binding.tvItem.text = itemset1.itemsets.toString()
            binding.tvJumlah.text = itemset1.length.toString()
            binding.tvSupport.text = itemset1.support.toString()
//            binding.item11.text = itemset1.itemsets.toString()
//            binding.jmlI1.text = itemset1.length.toString()
//            binding.supI1.text = itemset1.support.toString()
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
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class Itemset1lAdapter : ListAdapter<Itemset1LolosItem, Itemset1lAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(
        private var binding: ItemResultItemset1LBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset1l: Itemset1LolosItem) {

//            binding.item11.text = itemset1.itemsets.toString()
//            binding.jmlI1.text = itemset1.length.toString()
//            binding.supI1.text = itemset1.support.toString()
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
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class Itemset2Adapter : ListAdapter<Itemset2Item, Itemset2Adapter.ViewHolder>(DIFF_CALLBACK3) {
    class ViewHolder(
        private var binding: ItemResultItemset2Binding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset1l: Itemset1LolosItem) {

//            binding.item11.text = itemset1.itemsets.toString()
//            binding.jmlI1.text = itemset1.length.toString()
//            binding.supI1.text = itemset1.support.toString()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class Itemset2lAdapter : ListAdapter<Itemset2LolosItem, Itemset2lAdapter.ViewHolder>(DIFF_CALLBACK4) {
    class ViewHolder(
        private var binding: ItemResultItemset2LBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset1l: Itemset2LolosItem) {

//            binding.item11.text = itemset1.itemsets.toString()
//            binding.jmlI1.text = itemset1.length.toString()
//            binding.supI1.text = itemset1.support.toString()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class Itemset3Adapter : ListAdapter<Itemset3Item, Itemset3Adapter.ViewHolder>(DIFF_CALLBACK5) {
    class ViewHolder(
        private var binding: ItemResultItemset3Binding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset1l: Itemset3Item) {

//            binding.item11.text = itemset1.itemsets.toString()
//            binding.jmlI1.text = itemset1.length.toString()
//            binding.supI1.text = itemset1.support.toString()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

class Itemset3lAdapter : ListAdapter<Itemset3LolosItem, Itemset3lAdapter.ViewHolder>(DIFF_CALLBACK6) {
    class ViewHolder(
        private var binding: ItemResultItemset3LBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemset1l: Itemset3LolosItem) {

//            binding.item11.text = itemset1.itemsets.toString()
//            binding.jmlI1.text = itemset1.length.toString()
//            binding.supI1.text = itemset1.support.toString()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}

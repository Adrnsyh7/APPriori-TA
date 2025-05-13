package com.submission.tesapp.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.tesapp.adapter.Itemset1Adapter.ViewHolder.Companion.DIFF_CALLBACK1
import com.submission.tesapp.adapter.Itemset1lAdapter.ViewHolder.Companion.DIFF_CALLBACK
import com.submission.tesapp.adapter.Itemset2Adapter.ViewHolder.Companion.DIFF_CALLBACK3
import com.submission.tesapp.adapter.Itemset2lAdapter.ViewHolder.Companion.DIFF_CALLBACK4
import com.submission.tesapp.adapter.Itemset3Adapter.ViewHolder.Companion.DIFF_CALLBACK5
import com.submission.tesapp.adapter.Itemset3lAdapter.ViewHolder.Companion.DIFF_CALLBACK6
import com.submission.tesapp.adapter.ItemsetAssocAdapter.ViewHolder.Companion.DIFF_CALLBACK7
import com.submission.tesapp.data.response.AssociationRulesItem
import com.submission.tesapp.data.response.Itemset1Item
import com.submission.tesapp.data.response.Itemset1LolosItem
import com.submission.tesapp.data.response.Itemset2Item
import com.submission.tesapp.data.response.Itemset2LolosItem
import com.submission.tesapp.data.response.Itemset3Item
import com.submission.tesapp.data.response.Itemset3LolosItem
import com.submission.tesapp.databinding.ItemResultAssocBinding
import com.submission.tesapp.databinding.ItemResultBinding
import com.submission.tesapp.databinding.ItemResultItemset1LBinding
import com.submission.tesapp.databinding.ItemResultItemset2Binding
import com.submission.tesapp.databinding.ItemResultItemset2LBinding
import com.submission.tesapp.databinding.ItemResultItemset3Binding
import com.submission.tesapp.databinding.ItemResultItemset3LBinding
import java.time.LocalDate.now
import kotlin.random.Random

class Itemset1Adapter : ListAdapter<Itemset1Item, Itemset1Adapter.ViewHolder>(DIFF_CALLBACK1) {
    class ViewHolder(
        private var binding: ItemResultBinding,
        private val context: Context,
        private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(itemset1: Itemset1Item, position: Int) {
            binding.tvNo.text = (position + 1).toString()
            binding.tvItem.text = itemset1.item?.toString()
            binding.tvJumlah.text = itemset1.totalQuantity.toString()
            binding.tvSupport.text = itemset1.support.toString()
            binding.tvKeterangan.text = itemset1.keterangan

//            val itemset1Item = hashMapOf(
//                "item" to itemset1.item,
//                "keterangan" to itemset1.keterangan,
//                "qty" to itemset1.totalQuantity,
//                "support" to itemset1.support
//            )
//
//            db.collection("users").document("admin").collection("result").document("result" + now()).collection("itemset1").add(itemset1Item)
//                .addOnSuccessListener {
//                    Log.d(TAG, "berhasil")
//                }
//                .addOnFailureListener {
//                    it.printStackTrace()
//                }
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

    @RequiresApi(Build.VERSION_CODES.O)
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

//            val itemset1Iteml = hashMapOf(
//                "item" to itemset1l.item,
//                "keterangan" to itemset1l.keterangan,
//                "qty" to itemset1l.totalQuantity,
//                "support" to itemset1l.support
//            )
//
//            db.collection("users").document("admin").collection("result").document("result" + now()).collection("itemset1l").add(itemset1Iteml)
//                .addOnSuccessListener {
//                    Log.d(TAG, "berhasil")
//                }
//                .addOnFailureListener {
//                    it.printStackTrace()
//                }

            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem.text = itemset1l.item
                tvSupport.text= itemset1l.support.toString()
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
        private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(itemset2: Itemset2Item) {
//            val itemset2item = hashMapOf(
//                "item" to itemset2.itemsets1,
//                "item2" to itemset2.itemsets2,
//                "keterangan" to itemset2.keterangan,
//                "qty" to itemset2.count,
//                "support" to itemset2.support
//            )
//
//            db.collection("users").document("admin").collection("result").document("result" + now()).collection("itemset2").add(itemset2item)
//                .addOnSuccessListener {
//                    Log.d(TAG, "berhasil")
//                }
//                .addOnFailureListener {
//                    it.printStackTrace()
//                }


            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem1.text = itemset2.itemsets1.toString()
                tvItem2.text = itemset2.itemsets2.toString()
                tvSupport.text = itemset2.support.toString()
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Itemset2Adapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}

class Itemset2lAdapter : ListAdapter<Itemset2LolosItem, Itemset2lAdapter.ViewHolder>(DIFF_CALLBACK4) {
    class ViewHolder(
        private var binding: ItemResultItemset2LBinding,
        private val context: Context,
        private val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    ) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(itemset2l: Itemset2LolosItem) {
//            val itemset2litem = hashMapOf(
//                "item" to itemset2l.itemsets1,
//                "item2" to itemset2l.itemsets2,
//                "qty" to itemset2l.count,
//                "support" to itemset2l.support
//            )
//
//            db.collection("users").document("admin").collection("result").document("result" + now()).collection("itemset2l").add(itemset2litem)
//                .addOnSuccessListener {
//                    Log.d(TAG, "berhasil")
//                }
//                .addOnFailureListener {
//                    it.printStackTrace()
//                }

            with(binding) {
                with(binding) {
                    tvNo.text = (position + 1).toString()
                    tvItem1.text = itemset2l.itemsets1.toString()
                    tvItem2.text = itemset2l.itemsets2.toString()
                    tvSupport.text = itemset2l.support.toString()
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
                tvSupport.text = itemset3.support.toString()
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
                tvSupport.text = itemset3l.support.toString()
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
                tvItem.text = assoc.antecedents.toString() + "=>" + assoc.consequents.toString()
                tvConf.text = assoc.confidence.toString()
                tvLift.text = assoc.lift.toString()
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
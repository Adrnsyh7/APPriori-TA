package com.submission.tesapp.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.submission.tesapp.R
import com.submission.tesapp.adapter.Itemset11Adapter.ViewHolder.Companion.DIFF_CALLBACK11
import com.submission.tesapp.adapter.Itemset21Adapter.ViewHolder.Companion.DIFF_CALLBACK12
import com.submission.tesapp.adapter.Itemset31Adapter.ViewHolder.Companion.DIFF_CALLBACK13
import com.submission.tesapp.adapter.ItemsetAssoc1Adapter.ViewHolder.Companion.DIFF_CALLBACK14
import com.submission.tesapp.adapter.ResultFinalAdapter.ViewHolder.Companion.DIFF_CALLBACK15
import com.submission.tesapp.data.model.AssociationRule
import com.submission.tesapp.data.model.Itemset1
import com.submission.tesapp.data.model.Itemset2
import com.submission.tesapp.data.model.Itemset3
import com.submission.tesapp.data.model.ResultModel
import com.submission.tesapp.databinding.ItemReportBinding
import com.submission.tesapp.databinding.ItemResultAssocBinding
import com.submission.tesapp.databinding.ItemResultBinding
import com.submission.tesapp.databinding.ItemResultFinalBinding
import com.submission.tesapp.databinding.ItemResultItemset2Binding
import com.submission.tesapp.databinding.ItemResultItemset3Binding
import com.submission.tesapp.ui.report.ResultDetailActivity
import com.submission.tesapp.utils.DateConvert

class ReportAdapter : ListAdapter<ResultModel, ReportAdapter.ViewHolder>(DIFF_CALLBACK) {
    private var selectedItemId: String? = null

    class ViewHolder(
        private var binding: ItemReportBinding,
        private var context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(report: ResultModel, isSelected: Boolean) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                //tvNo.text = report.resultId
                tvFrom.text = DateConvert.convertDate(report.from?.toDate())
                tvTo.text = DateConvert.convertDate(report.end?.toDate())
                tvSupp.text = report.min_support.toString()
                tvConf.text = report.conf.toString()
                rootLayout.setBackgroundColor(
                    if (isSelected) Color.parseColor("#CCE5FF") else Color.TRANSPARENT
                )
            }


//            itemView.setOnClickListener {
//                val isSelected = position == selectedPosition
//                holder.itemView.isSelected = isSelected
//                val intent = Intent(context, ResultDetailActivity::class.java)
//                intent.putExtra(ResultDetailActivity.RESULTID, report)
//                context.startActivity(intent)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportAdapter.ViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportAdapter.ViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReportAdapter.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val dataItem = getItem(position)
        val isSelected = dataItem.resultId == selectedItemId // pastikan resultId adalah unik
        holder.bind(dataItem, isSelected)



        holder.itemView.setOnClickListener {
            val oldSelectedId = selectedItemId
            selectedItemId = dataItem.resultId
            notifyDataSetChanged()


            val intent = Intent(holder.itemView.context, ResultDetailActivity::class.java)
            intent.putExtra(ResultDetailActivity.RESULTID, dataItem)
            holder.itemView.context.startActivity(intent)
            if (holder.itemView.context is Activity) {
                (holder.itemView.context as Activity).overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
            }
//            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ResultModel>() {
            override fun areItemsTheSame(
                oldItem: ResultModel,
                newItem: ResultModel
            ): Boolean {
                return oldItem.resultId == newItem.resultId
            }

            override fun areContentsTheSame(
                oldItem: ResultModel,
                newItem: ResultModel
            ): Boolean {
                return oldItem == newItem
            }
        }
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
                tvItem1.text = itemset2.itemsets1
                tvItem2.text = itemset2.itemsets2
                tvSupport.text= itemset2.support.toString()
                tvJumlah.text = itemset2.count.toString()
                tvKeterangan.text = itemset2.keterangan.toString()
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
                tvItem1.text = Itemset3.itemsets1.toString()
                tvItem2.text = Itemset3.itemsets2.toString()
                tvItem3.text = Itemset3.itemsets3.toString()
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
        private var binding1: ItemResultFinalBinding,
        private val context: Context,

        ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemsetassoc: AssociationRule) {
            with(binding) {
                with(binding) {
                    tvNo.text = (position + 1).toString()
                    tvItem.text = itemsetassoc.antecedents.joinToString("") + "=>" + itemsetassoc.consequents.joinToString("")
                    tvLift.text = itemsetassoc.lift.toString()
                    tvConf.text = itemsetassoc.confidence.toString()

                    if(itemsetassoc.lift!! >= 1) {
                        tvKeterangan.text = "Korelasi Positif"
                    } else {
                        tvKeterangan.text = "Korelasi Negatif"
                    }
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
        val binding1 = ItemResultFinalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsetAssoc1Adapter.ViewHolder(binding, binding1, parent.context)
    }


    override fun onBindViewHolder(holder: ItemsetAssoc1Adapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}

class ResultFinalAdapter : ListAdapter<AssociationRule, ResultFinalAdapter.ViewHolder>(DIFF_CALLBACK15) {
    class ViewHolder(
        private var binding: ItemResultFinalBinding,
        private val context: Context,

        ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemsetassoc: AssociationRule) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvItem.text = "Jika konsumen membeli " + itemsetassoc.antecedents.joinToString("") + ", maka konsumen juga akan membeli " +
                        itemsetassoc.consequents.joinToString("")
                tvConf.text = itemsetassoc.confidence.toString()

            }
        }



        companion object {
            val DIFF_CALLBACK15 = object : DiffUtil.ItemCallback<AssociationRule>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultFinalAdapter.ViewHolder {
        val binding = ItemResultFinalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultFinalAdapter.ViewHolder(binding, parent.context)
    }


    override fun onBindViewHolder(holder: ResultFinalAdapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }
}
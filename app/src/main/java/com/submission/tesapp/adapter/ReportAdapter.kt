package com.submission.tesapp.adapter


import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.submission.tesapp.data.model.ResultModel
import com.submission.tesapp.databinding.ItemReportBinding
import com.submission.tesapp.ui.report.ResultDetailActivity
import com.submission.tesapp.utils.DateConvert

class ReportAdapter : ListAdapter<ResultModel, ReportAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(
        private var binding: ItemReportBinding,
        private var context: Context,
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(report: ResultModel, position: Int) {
            with(binding) {
                tvNo.text = (position + 1).toString()
                tvFrom.text = DateConvert.convertDate(report.from?.toDate())
                tvTo.text = DateConvert.convertDate(report.end?.toDate())
                tvSupp.text = report.min_support.toString()
                tvConf.text = report.conf.toString()
            }

            itemView.setOnClickListener {
                val intent = Intent(context, ResultDetailActivity::class.java)
                intent.putExtra(ResultDetailActivity.resultId, report)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportAdapter.ViewHolder {
        val binding = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportAdapter.ViewHolder(binding, parent.context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ReportAdapter.ViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem, position)
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
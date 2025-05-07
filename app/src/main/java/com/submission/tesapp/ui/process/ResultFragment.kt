package com.submission.tesapp.ui.process

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.tesapp.R
import com.submission.tesapp.ViewModelFactory
import com.submission.tesapp.adapter.Itemset1Adapter
import com.submission.tesapp.adapter.Itemset1lAdapter
import com.submission.tesapp.adapter.Itemset2Adapter
import com.submission.tesapp.adapter.Itemset2lAdapter
import com.submission.tesapp.adapter.Itemset3Adapter
import com.submission.tesapp.adapter.Itemset3lAdapter
import com.submission.tesapp.databinding.FragmentProcessBinding
import com.submission.tesapp.databinding.FragmentResultBinding


class ResultFragment : Fragment() {
    private lateinit var itemset1Adapter: Itemset1Adapter
    private lateinit var itemset1lAdapter: Itemset1lAdapter
    private lateinit var itemset2Adapter: Itemset2Adapter
    private lateinit var itemset2lAdapter: Itemset2lAdapter
    private lateinit var itemset3Adapter: Itemset3Adapter
    private lateinit var itemset3lAdapter: Itemset3lAdapter

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProcessViewModel> {
        ViewModelFactory.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemset1Adapter = Itemset1Adapter()
        itemset1lAdapter = Itemset1lAdapter()
        itemset2Adapter = Itemset2Adapter()
        itemset2lAdapter = Itemset2lAdapter()
        itemset3Adapter = Itemset3Adapter()
        itemset3lAdapter = Itemset3lAdapter()

        with(binding) {
            rvItemset1.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset1Adapter
            }
            rvItemset1lolos.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset1lAdapter
            }
            rvItemset2.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset2Adapter
            }
            rvItemset2lolos.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset2lAdapter
            }
            rvItemset3.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset3Adapter
            }
            rvItemset3lolos.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = itemset3lAdapter
            }

        }

    }
}
package com.submission.tesapp.ui.process

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.submission.tesapp.data.ResultState
import com.submission.tesapp.data.repository.AprioriRepository
import com.submission.tesapp.data.response.AprioriData
import com.submission.tesapp.data.response.AprioriResponses
import com.submission.tesapp.data.response.Itemset1Item

class ProcessViewModel(private val repository: AprioriRepository) : ViewModel() {
    fun fetchApriori(itemData: AprioriData) : LiveData<ResultState<AprioriResponses>> {
        return repository.processApriori(itemData)
    }

//    fun fetchItemset1(itemData: AprioriData): LiveData<ResultState<Itemset1Item>> {
//        return repository.
//    }

}
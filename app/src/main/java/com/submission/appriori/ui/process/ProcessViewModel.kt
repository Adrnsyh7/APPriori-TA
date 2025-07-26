package com.submission.appriori.ui.process

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.submission.appriori.data.ResultState
import com.submission.appriori.data.repository.AprioriRepository
import com.submission.appriori.data.response.AprioriData
import com.submission.appriori.data.response.AprioriResponses

class ProcessViewModel(private val repository: AprioriRepository) : ViewModel() {
    fun fetchApriori(itemData: AprioriData) : LiveData<ResultState<AprioriResponses>> {
        return repository.processApriori(itemData)
    }


}
package com.submission.tesapp.ui.process

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.submission.tesapp.data.ResultState
import com.submission.tesapp.data.repository.AprioriRepository
import com.submission.tesapp.data.response.AprioriData
import com.submission.tesapp.data.response.AprioriResponse

class ProcessViewModel(private val repository: AprioriRepository) : ViewModel() {
    fun fetchApriori(itemData: AprioriData) : LiveData<ResultState<AprioriResponse>> {
        return repository.processApriori(itemData)
    }

}
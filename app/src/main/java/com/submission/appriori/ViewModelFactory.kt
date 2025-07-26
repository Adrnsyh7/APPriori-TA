package com.submission.appriori

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.submission.appriori.data.repository.AprioriRepository
import com.submission.appriori.di.Injection
import com.submission.appriori.ui.process.ProcessViewModel

class ViewModelFactory private constructor(
    private val aprioriRepository: AprioriRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ProcessViewModel::class.java)) {
            return ProcessViewModel(aprioriRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.aprioriRepository(),
                )
            }.also { instance = it }
    }
}
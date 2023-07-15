package com.example.keepup.network.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.keepup.network.respository.NetworkRepository

class NetworkViewModelProviderFactory(
    val app: Application,
    private val networkRepository: NetworkRepository
    ) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NetworkViewModel(app, networkRepository) as T
    }
}
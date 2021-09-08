package com.example.agomastudio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agomastudio.Data.Event
import com.google.firebase.database.*

class ProviderEventViewModel : ViewModel() {
    private val _navigateToSearch = MutableLiveData<Boolean>()
    private lateinit var dbref: DatabaseReference
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

    fun onFabClicked(){
        _navigateToSearch.value = true
    }

    fun onNavigatedToSearch(){
        _navigateToSearch.value = false
    }


}
package com.learning.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.UserRepository
import com.learning.githubuser.data.model.ResponseSearchUser

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _searchResults = MutableLiveData<Resource<ResponseSearchUser>>()
    val searchResults: LiveData<Resource<ResponseSearchUser>> get() = _searchResults


    init {
        searchUsers("a")
    }

    fun searchUsers(query: String) {
        _searchResults.value = Resource.Loading()
        repository.searchUsers(query) { resource ->
            _searchResults.value = resource
        }
    }

}
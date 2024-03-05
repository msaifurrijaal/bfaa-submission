package com.learning.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.UserRepository
import com.learning.githubuser.data.model.ResponseSearchUser

class MainViewModel : ViewModel() {

    private val repository = UserRepository()

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
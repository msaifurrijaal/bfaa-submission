package com.learning.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.UserRepository
import com.learning.githubuser.data.model.ResponseDetailUser

class DetailViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _detailUserResult = MutableLiveData<Resource<ResponseDetailUser>>()
    val detailUserResult: LiveData<Resource<ResponseDetailUser>> get() = _detailUserResult

    fun searchDetailUser(username: String) {
        if (_detailUserResult.value !is Resource.Success && _detailUserResult.value !is Resource.Loading && _detailUserResult.value !is Resource.Error) {
            _detailUserResult.value = Resource.Loading()
            repository.getDetailUser(username) { resource ->
                _detailUserResult.value = resource
            }
        }
    }
}
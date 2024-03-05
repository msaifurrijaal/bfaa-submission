package com.learning.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.UserRepository
import com.learning.githubuser.data.model.ResponseItemFollow

class FollowingViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _listFollowingResult = MutableLiveData<Resource<List<ResponseItemFollow>>>()
    val listFollowingResult: LiveData<Resource<List<ResponseItemFollow>>> get() = _listFollowingResult

    fun getDataFollowing(username: String) {
        if (_listFollowingResult.value !is Resource.Success && _listFollowingResult.value !is Resource.Loading && _listFollowingResult.value !is Resource.Error) {
            _listFollowingResult.value = Resource.Loading()
            repository.getUserFollowing(username) { resource ->
                _listFollowingResult.value = resource
            }
        }
    }
}
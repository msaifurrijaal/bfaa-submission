package com.learning.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.UserRepository
import com.learning.githubuser.data.model.ResponseItemFollow

class FollowersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _listFollowerResult = MutableLiveData<Resource<List<ResponseItemFollow>>>()
    val listFollowerResult: LiveData<Resource<List<ResponseItemFollow>>> get() = _listFollowerResult

    fun getDataFollowers(username: String) {
        if (_listFollowerResult.value !is Resource.Success && _listFollowerResult.value !is Resource.Loading && _listFollowerResult.value !is Resource.Error) {
            _listFollowerResult.value = Resource.Loading()
            repository.getUserFollowers(username) { resource ->
                _listFollowerResult.value = resource
            }
        }
    }
}
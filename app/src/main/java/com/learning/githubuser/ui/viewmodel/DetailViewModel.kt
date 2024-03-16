package com.learning.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.UserRepository
import com.learning.githubuser.data.model.ResponseDetailUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _detailUserResult = MutableLiveData<Resource<ResponseDetailUser>>()
    val detailUserResult: LiveData<Resource<ResponseDetailUser>> get() = _detailUserResult

    suspend fun searchDetailUser(username: String) {
        viewModelScope.launch {
            if (_detailUserResult.value !is Resource.Success && _detailUserResult.value !is Resource.Loading && _detailUserResult.value !is Resource.Error) {
                _detailUserResult.value = Resource.Loading()
                repository.getDetailUser(username) { resource ->
                    _detailUserResult.value = resource
                }
            }
        }.join()
    }

    fun addFavUser(user: ResponseDetailUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavUser(user)
        }
    }

    fun deleteFavUser(user: ResponseDetailUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavUser(user)
        }
    }
}
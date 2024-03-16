package com.learning.githubuser.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.UserRepository
import com.learning.githubuser.data.model.ResponseDetailUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    suspend fun getFavUserList(): LiveData<Resource<List<ResponseDetailUser>>> = withContext(
        Dispatchers.IO
    ) {
        var favUserList: LiveData<Resource<List<ResponseDetailUser>>>? = null
        viewModelScope.launch {
            favUserList = repository.getFavUserList()
        }.join()
        return@withContext favUserList!!
    }

}
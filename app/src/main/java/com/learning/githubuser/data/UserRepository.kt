package com.learning.githubuser.data

import com.learning.githubuser.data.model.ResponseDetailUser
import com.learning.githubuser.data.model.ResponseItemFollow
import com.learning.githubuser.data.model.ResponseSearchUser
import com.learning.githubuser.data.remote.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    private val apiService = RetrofitInstance.getApiService()

    fun searchUsers(query: String, callback: (Resource<ResponseSearchUser>) -> Unit) {
        apiService.searchUsers(query).enqueue(object : Callback<ResponseSearchUser> {
            override fun onResponse(
                call: Call<ResponseSearchUser>,
                response: Response<ResponseSearchUser>
            ) {
                if (response.isSuccessful) {
                    callback(Resource.Success(response.body()))
                } else {
                    callback(Resource.Error("Failed to fetch users"))
                }
            }

            override fun onFailure(call: Call<ResponseSearchUser>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }

    fun getDetailUser(username: String, callback: (Resource<ResponseDetailUser>) -> Unit) {
        apiService.getDetailUser(username).enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                if (response.isSuccessful) {
                    callback(Resource.Success(response.body()))
                } else {
                    callback(Resource.Error("Failed to fetch user"))
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }

    fun getUserFollowers(username: String, callback: (Resource<List<ResponseItemFollow>>) -> Unit) {
        apiService.getFollowers(username).enqueue(object : Callback<List<ResponseItemFollow>> {
            override fun onResponse(
                call: Call<List<ResponseItemFollow>>,
                response: Response<List<ResponseItemFollow>>
            ) {
                if (response.isSuccessful) {
                    callback(Resource.Success(response.body()))
                } else {
                    callback(Resource.Error("Failed to fetch followers"))
                }
            }

            override fun onFailure(call: Call<List<ResponseItemFollow>>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }

    fun getUserFollowing(username: String, callback: (Resource<List<ResponseItemFollow>>) -> Unit) {
        apiService.getFollowing(username).enqueue(object : Callback<List<ResponseItemFollow>> {
            override fun onResponse(
                call: Call<List<ResponseItemFollow>>,
                response: Response<List<ResponseItemFollow>>
            ) {
                if (response.isSuccessful) {
                    callback(Resource.Success(response.body()))
                } else {
                    callback(Resource.Error("Failed to fetch followers"))
                }
            }

            override fun onFailure(call: Call<List<ResponseItemFollow>>, t: Throwable) {
                callback(Resource.Error(t.message))
            }
        })
    }
}
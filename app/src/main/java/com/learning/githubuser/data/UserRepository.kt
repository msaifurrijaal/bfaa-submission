package com.learning.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learning.githubuser.data.local.UserFavDB
import com.learning.githubuser.data.model.ResponseDetailUser
import com.learning.githubuser.data.model.ResponseItemFollow
import com.learning.githubuser.data.model.ResponseSearchUser
import com.learning.githubuser.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val application: Application) {

    private val apiService = RetrofitInstance.getApiService()
    private val dbUser: UserFavDB = UserFavDB.getInstance(application)
    private val dao = dbUser.userDao()

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

    suspend fun getDetailUser(
        username: String,
        callback: (Resource<ResponseDetailUser>) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val cachedUser = dao.getFavUser(username)

            if (cachedUser != null) {
                withContext(Dispatchers.Main) {
                    callback(Resource.Success(cachedUser))
                }
            } else {
                apiService.getDetailUser(username).enqueue(object : Callback<ResponseDetailUser> {
                    override fun onResponse(
                        call: Call<ResponseDetailUser>,
                        response: Response<ResponseDetailUser>
                    ) {
                        if (response.isSuccessful) {
                            val detail = response.body()
                            callback(Resource.Success(detail))

                        } else {
                            callback(Resource.Error(response.message()))
                        }
                    }

                    override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                        callback(Resource.Error(t.message.toString()))
                    }
                })
            }
        }
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

    suspend fun getFavUserList(): LiveData<Resource<List<ResponseDetailUser>>> = withContext(Dispatchers.IO) {

        val listFavUser = MutableLiveData<Resource<List<ResponseDetailUser>>>()
        listFavUser.postValue(Resource.Loading())

        if (dao.getAllFavUser().isNullOrEmpty())
            listFavUser.postValue(Resource.Error(null))
        else
            listFavUser.postValue(Resource.Success(dao.getAllFavUser()))

        return@withContext listFavUser
    }

    fun addFavUser(user: ResponseDetailUser) = dao.upsertFavUser(user)

    fun deleteFavUser(user: ResponseDetailUser) = dao.deleteFavUser(user)
}
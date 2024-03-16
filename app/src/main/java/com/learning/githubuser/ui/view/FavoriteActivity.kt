package com.learning.githubuser.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.githubuser.adapter.FavoriteAdapter
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.model.ResponseDetailUser
import com.learning.githubuser.databinding.ActivityFavoriteBinding
import com.learning.githubuser.ui.viewmodel.FavoriteViewModel
import com.learning.githubuser.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var usersAdapter: FavoriteAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        setFavUserAdapter()
        obsereveDataUser()
        onAction()
    }

    private fun setFavUserAdapter() {
        usersAdapter = FavoriteAdapter()
        binding.rvFavUser.apply {
            layoutManager =
                LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
            adapter = usersAdapter
        }
    }

    private fun onAction() {
        binding.apply {
            ibBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun obsereveDataUser() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.getFavUserList().observe(this@FavoriteActivity) { response ->
                when (response) {
                    is Resource.Error -> errorAction(response.message)
                    is Resource.Loading -> loadingAction()
                    is Resource.Success -> successAction(response.data)
                }
            }
        }
    }

    private fun successAction(data: List<ResponseDetailUser>?) {
        data.let {
            if (data != null) {
                usersAdapter.setGames(data)
            }
            if (data != null) {
                if (data.isEmpty()) {
                    errorAction("Data empty")
                } else {
                    noEmptyData()
                }
            }

            usersAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(user: ResponseDetailUser) {
                    val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                    intent.putExtra(Constants.USERNAME, user.login)
                    startActivity(intent)
                }
            })
        }
    }

    private fun noEmptyData() {
        binding.apply {
            pgFav.visibility = View.INVISIBLE
            ivEmpty.visibility = View.INVISIBLE
            tvMessage.visibility = View.INVISIBLE
            rvFavUser.visibility = View.VISIBLE
        }
    }

    private fun loadingAction() {
        binding.apply {
            rvFavUser.visibility = View.GONE
            ivEmpty.visibility = View.GONE
            tvMessage.visibility = View.GONE
            pgFav.visibility = View.VISIBLE
        }
    }

    private fun errorAction(message: String?) {
        binding.apply {
            rvFavUser.visibility = View.GONE
            pgFav.visibility = View.GONE
            ivEmpty.visibility = View.VISIBLE
            tvMessage.text = message
            tvMessage.visibility = View.VISIBLE
        }
    }

    override fun onRestart() {
        super.onRestart()
        obsereveDataUser()
    }
}
package com.learning.githubuser.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.githubuser.R
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.adapter.UserAdapter
import com.learning.githubuser.data.model.ResponseItemSearch
import com.learning.githubuser.data.model.ResponseSearchUser
import com.learning.githubuser.databinding.ActivityMainBinding
import com.learning.githubuser.ui.viewmodel.MainViewModel
import com.learning.githubuser.utils.Constants
import com.learning.githubuser.utils.hideSoftKeyboard

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    hideSoftKeyboard(this@MainActivity, binding.root)
                    viewModel.searchUsers(searchView.text.toString())
                    false
                }
        }

        observeDataUsers()

    }

    private fun observeDataUsers() {
        viewModel.searchResults.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> showSearchResults(resource.data)
                is Resource.Error -> showError(resource.message)
                else -> {
                    showError(getString(R.string.error_message))
                }
            }
        }
    }

    private fun showSearchResults(data: ResponseSearchUser?) {
        data?.let {
            val usersAdapter = UserAdapter(data.items as List<ResponseItemSearch>)
            binding.apply {
                rvUserMain.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = usersAdapter
                    setHasFixedSize(true)
                }
                rvUserSearch.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = usersAdapter
                    setHasFixedSize(true)
                }
            }
            if (data.items.isEmpty()) {
                emptyData()
            } else {
                noEmptyData()
            }

            usersAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: ResponseItemSearch) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(Constants.USERNAME, user.login)
                    startActivity(intent)
                }
            })
        }
    }

    private fun showLoading() {
        binding.apply {
            rvUserSearch.visibility = View.GONE
            rvUserMain.visibility = View.GONE
            containerMessageMain.visibility = View.GONE
            containerMessageSearch.visibility = View.GONE
            pgMain.visibility = View.VISIBLE
            pgSearch.visibility = View.VISIBLE
        }
    }

    private fun noEmptyData() {
        binding.apply {
            pgMain.visibility = View.GONE
            pgSearch.visibility = View.GONE
            containerMessageMain.visibility = View.GONE
            containerMessageSearch.visibility = View.GONE
            rvUserSearch.visibility = View.VISIBLE
            rvUserMain.visibility = View.VISIBLE
        }
    }

    private fun emptyData() {
        binding.apply {
            pgMain.visibility = View.GONE
            pgSearch.visibility = View.GONE
            rvUserSearch.visibility = View.GONE
            rvUserMain.visibility = View.GONE
            containerMessageMain.visibility = View.VISIBLE
            containerMessageSearch.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String?) {
        binding.apply {
            rvUserSearch.visibility = View.GONE
            rvUserMain.visibility = View.GONE
            pgMain.visibility = View.GONE
            pgSearch.visibility = View.GONE
            containerMessageMain.visibility = View.VISIBLE
            containerMessageSearch.visibility = View.VISIBLE
            tvMessageMain.text = message
            tvMessageSearch.text = message
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                Toast.makeText(
                    this,
                    getString(R.string.favorite_feature_on_development), Toast.LENGTH_SHORT
                ).show()
            }

            R.id.setting -> {
                Toast.makeText(
                    this,
                    getString(R.string.settings_feature_on_development), Toast.LENGTH_SHORT
                ).show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
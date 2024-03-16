package com.learning.githubuser.ui.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.githubuser.R
import com.learning.githubuser.data.Resource
import com.learning.githubuser.adapter.UserAdapter
import com.learning.githubuser.data.datastore.SettingPreferences
import com.learning.githubuser.data.model.ResponseItemSearch
import com.learning.githubuser.data.model.ResponseSearchUser
import com.learning.githubuser.databinding.ActivityMainBinding
import com.learning.githubuser.ui.viewmodel.MainViewModel
import com.learning.githubuser.ui.viewmodel.SettingViewModel
import com.learning.githubuser.ui.viewmodel.ViewModelFactory
import com.learning.githubuser.utils.Constants
import com.learning.githubuser.utils.hideSoftKeyboard

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref)).get(SettingViewModel::class.java)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    hideSoftKeyboard(this@MainActivity, binding.root)
                    mainViewModel.searchUsers(searchView.text.toString())
                    false
                }
        }

        observeTheme()
        observeDataUsers()

    }

    private fun observeTheme() {
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun observeDataUsers() {
        mainViewModel.searchResults.observe(this) { resource ->
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
                startActivity(
                    Intent(this@MainActivity, FavoriteActivity::class.java)
                )
            }

            R.id.setting -> {
                startActivity(
                    Intent(this@MainActivity, SettingActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
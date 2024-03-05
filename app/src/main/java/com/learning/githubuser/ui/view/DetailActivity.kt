package com.learning.githubuser.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.learning.githubuser.R
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.adapter.TabsAdapter
import com.learning.githubuser.data.model.ResponseDetailUser
import com.learning.githubuser.databinding.ActivityDetailBinding
import com.learning.githubuser.ui.viewmodel.DetailViewModel
import com.learning.githubuser.utils.Constants

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var username: String
    private lateinit var tabsFollowAdapter: TabsAdapter
    private var urlAccount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        getInformationFromIntent()
        onAction()
        observeDataUser()
        setViewPager()

    }

    private fun onAction() {
        binding.apply {
            ibBack.setOnClickListener {
                finish()
            }

            ibShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Information about account\nGoes to profile page $urlAccount"
                    )
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    private fun setViewPager() {
        tabsFollowAdapter = TabsAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs
        tabsFollowAdapter.setMyString(username)
        viewPager.adapter = tabsFollowAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TITLES_TAB[position])
        }.attach()
    }

    private fun observeDataUser() {
        viewModel.searchDetailUser(username)
        viewModel.detailUserResult.observe(this) { resources ->
            when (resources) {
                is Resource.Error -> {
                    showError(resources.message)
                }

                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    showDetailResult(resources.data)
                }

                else -> {
                    showError(getString(R.string.error_message))
                }
            }
        }
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDetailResult(data: ResponseDetailUser?) {
        data?.let {
            binding.apply {
                Glide.with(this@DetailActivity)
                    .load(data.avatarUrl)
                    .into(ivUser)
                tvUserFullname.text = data.name
                tvUserFollowers.text = data.followers.toString()
                tvUserFollowing.text = data.following.toString()
                tvUserRepository.text = data.publicRepos.toString()
                tvUsername.text = data.login
                urlAccount = data.url!!

                ivUser.visibility = View.VISIBLE
                pgMain.visibility = View.GONE
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            ivUser.visibility = View.INVISIBLE
            pgMain.visibility = View.VISIBLE
        }
    }


    private fun getInformationFromIntent() {
        username = intent.getStringExtra(Constants.USERNAME) ?: ""
    }

    companion object {
        private val TITLES_TAB = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
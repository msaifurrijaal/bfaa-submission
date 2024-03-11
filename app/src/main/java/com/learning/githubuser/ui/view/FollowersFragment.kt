package com.learning.githubuser.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.githubuser.R
import com.learning.githubuser.data.Resource
import com.learning.githubuser.adapter.FollowAdapter
import com.learning.githubuser.data.model.ResponseItemFollow
import com.learning.githubuser.databinding.FragmentFollowersBinding
import com.learning.githubuser.ui.viewmodel.FollowersViewModel
import com.learning.githubuser.utils.Constants

class FollowersFragment : Fragment() {

    private var username: String? = null
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var followersAdapter: FollowAdapter
    private lateinit var viewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)

        username = arguments?.getString(Constants.USERNAME)
        viewModel = ViewModelProvider(this)[FollowersViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataFollowers()
    }


    private fun observeDataFollowers() {
        username?.let {
            viewModel.getDataFollowers(username!!)
        }
        viewModel.listFollowerResult.observe(viewLifecycleOwner) { resources ->
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

    private fun showLoading() {
        binding.apply {
            rvFollow.visibility = View.GONE
            tvMessage.visibility = View.GONE
            pgFollow.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String?) {
        binding.apply {
            pgFollow.visibility = View.GONE
            rvFollow.visibility = View.GONE
            tvMessage.text = message
            tvMessage.visibility = View.VISIBLE
        }
    }

    private fun showDetailResult(data: List<ResponseItemFollow>?) {
        data?.let {
            followersAdapter = FollowAdapter(data as ArrayList<ResponseItemFollow>)
            binding.apply {
                rvFollow.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = followersAdapter
                    setHasFixedSize(true)
                }
            }
            if (data.isEmpty()) {
                emptyData()
            } else {
                noEmptyData()
            }
        }
    }

    private fun noEmptyData() {
        binding.apply {
            pgFollow.visibility = View.GONE
            tvMessage.visibility = View.GONE
            rvFollow.visibility = View.VISIBLE
        }
    }

    private fun emptyData() {
        binding.apply {
            pgFollow.visibility = View.GONE
            rvFollow.visibility = View.GONE
            tvMessage.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
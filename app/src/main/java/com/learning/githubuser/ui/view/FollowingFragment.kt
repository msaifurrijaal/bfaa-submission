package com.learning.githubuser.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.learning.githubuser.R
import com.learning.githubuser.data.Resource
import com.learning.githubuser.data.adapter.FollowAdapter
import com.learning.githubuser.data.model.ResponseItemFollow
import com.learning.githubuser.databinding.FragmentFollowingBinding
import com.learning.githubuser.ui.viewmodel.FollowViewModel
import com.learning.githubuser.utils.Constants


class FollowingFragment : Fragment() {

    private var username: String? = null
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingAdapter: FollowAdapter
    private lateinit var viewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        username = arguments?.getString(Constants.USERNAME)
        viewModel = ViewModelProvider(this)[FollowViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeDataFollowing()
    }

    private fun observeDataFollowing() {
        username?.let {
            viewModel.getDataFollowing(username!!)
        }
        viewModel.listFollowingResult.observe(viewLifecycleOwner) { resources ->
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
            tvMessage.visibility = View.VISIBLE
            pgFollowFollowing.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String?) {
        binding.apply {
            pgFollowFollowing.visibility = View.GONE
            rvFollow.visibility = View.GONE
            tvMessage.text = message
            tvMessage.visibility = View.VISIBLE
        }
    }

    private fun showDetailResult(data: List<ResponseItemFollow>?) {
        data?.let {
            followingAdapter = FollowAdapter(data as ArrayList<ResponseItemFollow>)
            binding.apply {
                rvFollow.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = followingAdapter
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
            pgFollowFollowing.visibility = View.GONE
            tvMessage.visibility = View.GONE
            rvFollow.visibility = View.VISIBLE
        }
    }

    private fun emptyData() {
        binding.apply {
            pgFollowFollowing.visibility = View.GONE
            rvFollow.visibility = View.GONE
            tvMessage.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
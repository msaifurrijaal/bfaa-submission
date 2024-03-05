package com.learning.githubuser.data.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.learning.githubuser.ui.view.FollowersFragment
import com.learning.githubuser.ui.view.FollowingFragment
import com.learning.githubuser.utils.Constants

class TabsAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private var username: String? = null

    fun setMyString(user: String) {
        username = user
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = Bundle().apply {
            putString(Constants.USERNAME, username)
        }
        return fragment!!
    }
}
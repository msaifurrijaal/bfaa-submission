package com.learning.githubuser.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.githubuser.data.model.ResponseItemFollow
import com.learning.githubuser.databinding.ItemUserBinding

class FollowAdapter(private val listUser: ArrayList<ResponseItemFollow>) :
    RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]

        holder.binding.apply {
            tvUsername.text = user.login
            Glide.with(holder.itemView.context)
                .load(user.avatarUrl)
                .into(imgUser)
        }
    }
}
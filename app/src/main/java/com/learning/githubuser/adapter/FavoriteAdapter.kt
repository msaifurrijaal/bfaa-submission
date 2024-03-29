package com.learning.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.learning.githubuser.data.model.ResponseDetailUser
import com.learning.githubuser.databinding.ItemUserBinding

class FavoriteAdapter :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private var listUser = ArrayList<ResponseDetailUser>()

    @SuppressLint("NotifyDataSetChanged")
    fun setGames(newList: List<ResponseDetailUser>) {
        this.listUser.clear()
        this.listUser.addAll(newList)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

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

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(user) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: ResponseDetailUser)
    }
}
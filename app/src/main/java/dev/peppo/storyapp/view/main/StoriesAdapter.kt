package dev.peppo.storyapp.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.peppo.storyapp.data.remote.response.story.ListStoryItem
import dev.peppo.storyapp.databinding.ItemRowStoryBinding

class StoriesAdapter(private val stories: ArrayList<ListStoryItem>) : RecyclerView.Adapter<StoriesAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = stories.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        Glide.with(holder.binding.cardView)
            .load(stories[position].photoUrl)
            .into(holder.binding.imgStory)
        holder.binding.titleStory.text = stories[position].name
        holder.binding.descStory.text = stories[position].description

        holder.binding.cardView.setOnClickListener {
            onItemClickCallback.onItemClicked(stories[holder.adapterPosition])
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}
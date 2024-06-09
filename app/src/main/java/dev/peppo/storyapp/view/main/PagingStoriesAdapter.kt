package dev.peppo.storyapp.view.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.peppo.storyapp.data.remote.response.story.ListStoryItem
import dev.peppo.storyapp.databinding.ItemRowStoryBinding
import dev.peppo.storyapp.view.detailstory.DetailStoryActivity

class PagingStoriesAdapter :
    PagingDataAdapter<ListStoryItem, PagingStoriesAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        Log.d("testDataNull", data.toString())
        if (data != null) {
            holder.bind(data)
            userClickHandler(holder, data)
        }
    }

    private fun userClickHandler(holder: MyViewHolder, data: ListStoryItem) {
        holder.itemView.setOnClickListener {
            val toDetailIntent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            toDetailIntent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
            holder.itemView.context.startActivity(toDetailIntent)
        }
    }

    class MyViewHolder(private val binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            Glide.with(binding.cardView)
                .load(data.photoUrl)
                .into(binding.imgStory)
            binding.titleStory.text = data.name
            binding.descStory.text = data.description
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
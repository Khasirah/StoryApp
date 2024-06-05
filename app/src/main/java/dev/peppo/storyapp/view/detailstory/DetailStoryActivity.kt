package dev.peppo.storyapp.view.detailstory

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import dev.peppo.storyapp.data.remote.response.story.ListStoryItem
import dev.peppo.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_STORY, ListStoryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_STORY)
        }

        showDetailStory(story)
    }

    private fun showDetailStory(story: ListStoryItem?) {
        if (story != null) {
            binding.apply {
                detailName.text = story.name
                detailDescription.text = story.description
                Glide.with(scrollView)
                    .load(story.photoUrl)
                    .into(detailImg)
            }
        }
    }
}
package dev.peppo.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dev.peppo.storyapp.R
import dev.peppo.storyapp.data.Result
import dev.peppo.storyapp.data.remote.response.story.ListStoryItem
import dev.peppo.storyapp.databinding.ActivityMainBinding
import dev.peppo.storyapp.utils.ViewModelFactory
import dev.peppo.storyapp.view.createstory.CreateStoryActivity
import dev.peppo.storyapp.view.detailstory.DetailStoryActivity
import dev.peppo.storyapp.view.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = binding.toolbar
        val factory = ViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels<MainViewModel> { factory }

        setupToolbar(toolbar)
        setupRecycleView(mainViewModel)

        toolbar.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId) {
                R.id.addButton -> {
                    val toCreateStoryIntent = Intent(this@MainActivity, CreateStoryActivity::class.java)
                    startActivity(toCreateStoryIntent)
                    true
                }
                R.id.logoutButton -> {
                    processLogout(mainViewModel)
                    true
                }
                else -> false
            }
        }
    }

    private fun processLogout(mainViewModel: MainViewModel) {
        mainViewModel.logout()
        val toLoginIntent = Intent(this, LoginActivity::class.java)
        toLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(toLoginIntent)
        finish()
    }

    private fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupRecycleView(mainViewModel: MainViewModel) {
        binding.rvStories.setHasFixedSize(true)
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        mainViewModel.getStories().observe(this) {
            if (it != null) {
                when(it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val storiesAdapter = StoriesAdapter(it.data.listStory as ArrayList)
                        binding.rvStories.adapter = storiesAdapter

                        storiesAdapter.setOnItemClickCallback(object : StoriesAdapter.OnItemClickCallback {
                            override fun onItemClicked(data: ListStoryItem) {
                                val toDetailIntent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                                toDetailIntent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
                                startActivity(toDetailIntent)

                            }
                        })
                    }
                    is Result.Error -> {
                        showLoading(false)
                        val toLoginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(toLoginIntent)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
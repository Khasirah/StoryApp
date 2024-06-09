package dev.peppo.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dev.peppo.storyapp.R
import dev.peppo.storyapp.databinding.ActivityMainBinding
import dev.peppo.storyapp.utils.ViewModelFactory
import dev.peppo.storyapp.view.adapter.LoadingStateAdapter
import dev.peppo.storyapp.view.createstory.CreateStoryActivity
import dev.peppo.storyapp.view.login.LoginActivity
import dev.peppo.storyapp.view.map.MapsActivity

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

        checkToken(mainViewModel)
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
                R.id.mapButton -> {
                    val toMapIntent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(toMapIntent)
                    true
                }
                else -> false
            }
        }
    }

    private fun checkToken(mainViewModel: MainViewModel) {
        mainViewModel.getToken().observe(this) {
            if (it == "null") {
                val toLoginIntent = Intent(this, LoginActivity::class.java)
                startActivity(toLoginIntent)
                finish()
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
        val pagingStoriesAdapter = PagingStoriesAdapter()
        binding.rvStories.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = pagingStoriesAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    pagingStoriesAdapter.retry()
                }
            )
        }
        mainViewModel.getStories().observe(this) {
            pagingStoriesAdapter.submitData(lifecycle, it)
        }
    }
}
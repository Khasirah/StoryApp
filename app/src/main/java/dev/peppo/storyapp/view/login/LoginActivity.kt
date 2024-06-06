package dev.peppo.storyapp.view.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.peppo.storyapp.data.Result
import dev.peppo.storyapp.data.remote.response.login.LoginResponse
import dev.peppo.storyapp.databinding.ActivityLoginBinding
import dev.peppo.storyapp.utils.AppPreferences
import dev.peppo.storyapp.utils.ViewModelFactory
import dev.peppo.storyapp.utils.dataStore
import dev.peppo.storyapp.view.main.MainActivity
import dev.peppo.storyapp.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory = ViewModelFactory.getInstance(this)
        val loginViewModel: LoginViewModel by viewModels<LoginViewModel> { factory }
        playAnimation()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            loginViewModel.login(email, password).observe(this@LoginActivity) {
                if (it != null) {
                    when(it) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            processLogin(it.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showToast(it.error)
                        }
                    }
                }
            }
        }

        binding.registerButton.setOnClickListener {
            val toRegisterIntent = Intent(this, RegisterActivity::class.java)
            startActivity(toRegisterIntent)
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun processLogin(data: LoginResponse) {
        if (data.error) {
            showToast(data.message.toString())
        } else {
            val toMainIntent = Intent(this, MainActivity::class.java)
            startActivity(toMainIntent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
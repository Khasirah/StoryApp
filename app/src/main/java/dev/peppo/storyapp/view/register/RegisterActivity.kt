package dev.peppo.storyapp.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.peppo.storyapp.data.Result
import dev.peppo.storyapp.data.remote.response.register.RegisterResponse
import dev.peppo.storyapp.databinding.ActivityRegisterBinding
import dev.peppo.storyapp.utils.AppPreferences
import dev.peppo.storyapp.utils.ViewModelFactory
import dev.peppo.storyapp.utils.dataStore
import dev.peppo.storyapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val registerViewModel: RegisterViewModel by viewModels<RegisterViewModel> {
            factory
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            registerViewModel.register(name, email, password).observe(this) {
                if (it != null) {
                    when(it) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            processRegister(it.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showToast(it.error)
                        }
                    }
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val toLoginIntent = Intent(this, LoginActivity::class.java)
            startActivity(toLoginIntent)
        }
    }

    private fun processRegister(data: RegisterResponse) {
        if (data.error) {
            showToast("Gagal Daftar")
        } else {
            showToast("Berhasil daftar, silahkan login!")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
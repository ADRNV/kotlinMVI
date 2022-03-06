package com.example.kotlinmvi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinmvi.viewmodels.MainViewModel
import com.example.kotlinmvi.databinding.ActivityMainBinding
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.kotlinmvi.viewmodels.MainContract
import kotlinx.coroutines.flow.collect
import android.content.Intent
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        initObservers()

        setContentView(binding.root)

        binding.generateNumber.setOnClickListener {
            viewModel.setEvent(MainContract.Event.OnRandomNumberClicked)
        }

        binding.rangeBtn.setOnClickListener {viewModel.setEvent(MainContract.Event.OnAddRange)}

        binding.showToast.setOnClickListener {
            viewModel.setEvent(MainContract.Event.OnShowToastByClick)
        }


        binding.secondActivity.setOnClickListener{startActivity(Intent(this,SecondActivity::class.java))}

    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {

            viewModel.uiState.collect {
                when (it.randomNumberState) {
                    is MainContract.RandomNumberState.Idle -> { binding.progressBar.isVisible = false }
                    is MainContract.RandomNumberState.Loading -> { binding.progressBar.isVisible = true }
                    is MainContract.RandomNumberState.Success -> {
                        binding.progressBar.isVisible = false
                        binding.number.text = it.randomNumberState.number.toString()
                    }

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect {
                when (it) {
                    is MainContract.Effect.ShowToast -> {
                        binding.progressBar.isVisible = false
                        showToast("Error, number is even")
                    }
                    is MainContract.Effect.ShowToastByClick -> {

                        binding.progressBar.isVisible = false
                        showToast(this.toString())
                    }
                    is MainContract.Effect.ShowAddRangeToast -> {
                        binding.progressBar.isVisible = false
                        showToast("Range +1")
                    }
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }


}
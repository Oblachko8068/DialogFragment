package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.showDialogvar.setOnClickListener {
            val is_day_theme = true
            val dialogFragment =
                EstimateDialogFragment.newInstance(is_day_theme)
            dialogFragment.show(supportFragmentManager, "TestFragmentTag")
        }
        supportFragmentManager.setFragmentResultListener("result", this) { _, result ->
            val estimate = result.getInt("estimate")
            val badEstimateFragment =
                BadEstimateDialogFragment.newInstance(mainViewModel, true, estimate)
            badEstimateFragment.show(
                supportFragmentManager,
                "BadEstimateFragmentTag"
            )
        }
    }
}
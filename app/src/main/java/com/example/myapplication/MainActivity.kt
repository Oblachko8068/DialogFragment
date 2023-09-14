package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.provider.Settings
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
        supportFragmentManager.setFragmentResultListener("firstResult", this) { _, result ->
            val estimate = result.getInt("estimate")
            if(estimate >= 3){
                mainViewModel.loadInfo(object : RequestInterface<String> {
                    override fun onSuccess(data: String) {
                        sentGoodEstimate(estimate.toString(), data)
                    }

                    override fun onFailure(errorData: ErrorData) {
                        sentGoodEstimate(estimate.toString(), "")
                    }
                })
            } else {
                val badEstimateFragment =
                    BadEstimateDialogFragment.newInstance(true, estimate)
                badEstimateFragment.show(supportFragmentManager, "BadEstimateFragmentTag")
                supportFragmentManager.setFragmentResultListener("badResult", this){_,result ->
                    val estimateRating = result.getString("estimateRating")
                    val androidID = result.getString("androidID")
                    val stringTextEmail = result.getString("stringTextEmail")
                    val stringTextEstimate = result.getString("stringTextEstimate")
                    val userAgent = result.getString("userAgent")

                    sendEstimate(
                        estimateRating!!,
                        androidID!!,
                        stringTextEmail!!,
                        stringTextEstimate!!,
                        userAgent!!
                    )
                }
            }
        }
    }

    private fun sentGoodEstimate(estimateRating: String, info: String) {
        val userAgent = UserAgent.getXLHDAgent(this)
        @SuppressLint("HardwareIds") var androidID =
            Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        if (androidID == null) {
            androidID = ""
        }
        sendEstimate(estimateRating, androidID, "", info, userAgent)
    }

    private fun sendEstimate(
        value: String,
        dvid: String,
        email: String?,
        comment: String,
        userAgent: String
    ) {
        val ratingRepository = provideRatingRepository(this)
        mainViewModel.loadInfo(object : RequestInterface<String> {
            override fun onSuccess(data: String) {
                ratingRepository.send(
                    email ?: "",
                    data + UserAgent.getDevInfo(this) + comment,
                    value
                )
            }

            override fun onFailure(errorData: ErrorData) {
                ratingRepository.send(
                    email ?: "",
                    "Error info" + UserAgent.getDevInfo(this) + comment,
                    value
                )
            }
        })
    }
    //Заглушка
    private fun provideRatingRepository(activity: Activity): RatingRepository {
        return DummyRatingRepository()
    }

}
package com.example.myapplication

import android.app.Activity
import androidx.fragment.app.DialogFragment

open class BaseEstimateDialogFragment : DialogFragment() {

    open val mainViewModel = MainViewModel()

    open fun dismissDialog() {
        dismiss()
    }

    open fun sendEstimate(
        value: String,
        dvid: String,
        email: String?,
        comment: String,
        userAgent: String
    ) {
        val ratingRepository = provideRatingRepository(requireActivity())
        mainViewModel.loadInfo(object : RequestInterface<String> {
            override fun onSuccess(data: String) {
                ratingRepository.send(
                    email ?: "",
                    data + UserAgent.getDevInfo(requireActivity()) + comment,
                    value
                )
            }

            override fun onFailure(errorData: ErrorData) {
                ratingRepository.send(
                    email ?: "",
                    "Error info" + UserAgent.getDevInfo(requireActivity()) + comment,
                    value
                )
            }
        })
    }
    //Заглушка
    open fun provideRatingRepository(activity: Activity): RatingRepository {
        return DummyRatingRepository()
    }

}
package com.example.myapplication

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel

interface RatingRepository {
    fun send(s: String, s1: String, value: String)
}

class DummyRatingRepository : RatingRepository {
    override fun send(s: String, s1: String, value: String) {
    }
}


data class ErrorData(val message: String)
interface RequestInterface<T> {
    fun onSuccess(data: T)
    fun onFailure(errorData: ErrorData)
}

class UserAgent {
    companion object {
        fun getXLHDAgent(requireActivity: FragmentActivity): String {
            return ""
        }

        fun getDevInfo(requireActivity: RequestInterface<String>): Any {
            return ""
        }
    }
}

class MainViewModel : ViewModel() {
    fun loadInfo(any: Any) {
    }

}

class ApplicationStatisticsReporter {
    companion object {
        fun sendRateAppGrade(defaultEstimate: Int) : Int {
            return defaultEstimate
        }
    }
}


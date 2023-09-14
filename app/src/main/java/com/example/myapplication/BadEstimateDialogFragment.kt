package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.myapplication.databinding.BadEstimateDialogBinding
import java.lang.NullPointerException

class BadEstimateDialogFragment : DialogFragment() {

    private var default_estimate = 0
    private lateinit var binding: BadEstimateDialogBinding
    //private lateinit var bindingNightBad: BadEstimateDialogNightBinding
    private var isDayTheme: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            default_estimate = it.getInt(DEFAULT_ESTIMATE_DATA)
            isDayTheme = it.getBoolean(IS_DAY_BAD_THEME_DATA)
        }
        binding = BadEstimateDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelEstimate.setOnClickListener { dismissDialog() }
        binding.sendEstimate.setOnClickListener { sendBadEstimate() }
    }

    @SuppressLint("HardwareIds")
    private fun sendBadEstimate() {
        try {
            val estimateRating = default_estimate.toString()
            val userAgent = UserAgent.getXLHDAgent(requireActivity())
            val stringTextEmail = binding.emailEstimate.text.toString()
            val stringTextEstimate = binding.textEstimate.text.toString()

            if (stringTextEmail.isNotEmpty()) {
                if (isValidEmail(stringTextEmail)) {
                    @SuppressLint("HardwareIds") var androidID =
                        Settings.Secure.getString(
                            requireActivity().contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    if (androidID == null) {
                        androidID = ""
                    }

                    val resultData = Bundle()
                    resultData.putString("estimateRating", estimateRating)
                    resultData.putString("androidID", androidID)
                    resultData.putString("stringTextEmail", stringTextEmail)
                    resultData.putString("stringTextEstimate", stringTextEstimate)
                    resultData.putString("userAgent", userAgent)
                    setFragmentResult("badResult", resultData)

                    dismissDialog()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Неверный формат EMAIL",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                val androidID =
                    Settings.Secure.getString(
                        requireActivity().contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                        ?: ""

                val resultData = Bundle()
                resultData.putString("estimateRating", estimateRating)
                resultData.putString("androidID", androidID)
                resultData.putString("stringTextEmail", stringTextEmail)
                resultData.putString("stringTextEstimate", stringTextEstimate)
                resultData.putString("userAgent", userAgent)
                setFragmentResult("badResult", resultData)

                dismissDialog()
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
    private fun dismissDialog() {
        dismiss()
    }


    private fun isValidEmail(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        @JvmStatic
        private val IS_DAY_BAD_THEME_DATA = "is_day_bad_theme_data"
        private const val DEFAULT_ESTIMATE_DATA = "default_estimate_data"

        @JvmStatic
        fun newInstance(
            isDayTheme: Boolean,
            default_estimate: Int
        ) = BadEstimateDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_DAY_BAD_THEME_DATA, isDayTheme)
                    putInt(DEFAULT_ESTIMATE_DATA, default_estimate)
                }
        }
    }
}

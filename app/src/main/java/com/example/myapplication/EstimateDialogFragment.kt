package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.myapplication.databinding.EstimateDialogBinding


class EstimateDialogFragment : DialogFragment() {

    private var default_estimate = 5
    private var isDayTheme: Boolean = true
    private lateinit var binding: EstimateDialogBinding

    /*override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.container.setBackgroundColor(Color.WHITE)
            } // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.container.setBackgroundColor(Color.BLACK)
            } // Night mode is active, we're using dark theme
        }
    }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            isDayTheme = it.getBoolean(IS_DAY_THEME_DATA)
        }
        if (savedInstanceState != null) {
            default_estimate = savedInstanceState.getInt(DEFAULT_ESTIMATE_DATA)
        }
        binding = EstimateDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val starList = listOf(
            binding.starOne,
            binding.starTwo,
            binding.starThree,
            binding.starFour,
            binding.starFive
        )
        starList.forEachIndexed { index, starImageView ->
            starImageView.setOnClickListener {
                onStarClick(index, starList)
            }
        }
        if (default_estimate != 5){
            onStarClick(default_estimate, starList)
        }

        binding.cancelEstimate.setOnClickListener { dismissDialog() }
        binding.sendEstimate.setOnClickListener { sendEstimateClick() }
    }

    private fun onStarClick(estimate: Int, starList: List<ImageView>) {
        for (i in 0 until 5) {
            if (i <= estimate) {
                starList[i].setImageResource(R.drawable.channels_ic_favourite_on)
            } else {
                starList[i].setImageResource(R.drawable.channels_ic_favourite_off)
            }
            starList[i].setColorFilter(
                starList[i].context.resources.getColor(R.color.colorStar),
                PorterDuff.Mode.SRC_ATOP
            )
        }
        default_estimate = estimate
    }

    private fun sendEstimateClick() {
        ApplicationStatisticsReporter.sendRateAppGrade(default_estimate)
        //dismissDialog()
        if (default_estimate > 3) {

            val resultData = Bundle()
            resultData.putInt("estimate", default_estimate)
            setFragmentResult("firstResult", resultData)

            val handler = Handler()
            handler.postDelayed({
                dismissDialog()
                requestPlayMarket()
            }, 1000)
        } else {
            dismissDialog()

            val resultData = Bundle()
            resultData.putInt("estimate", default_estimate)
            setFragmentResult("firstResult", resultData)
        }
        default_estimate = 5
    }

    private fun requestPlayMarket() {
        try {
            val appPackageName: String = requireActivity().packageName
            try {
                requireActivity().startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "market://details?id=$appPackageName"
                        )
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                requireActivity().startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "http://play.google.com/store/apps/details?id=$appPackageName"
                        )
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dismissDialog() {
        dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(DEFAULT_ESTIMATE_DATA, default_estimate)
    }

    companion object {
        @JvmStatic
        private val IS_DAY_THEME_DATA = "is_day_theme_data"
        private const val DEFAULT_ESTIMATE_DATA = "default_estimate_data"

        @JvmStatic
        fun newInstance(isDayTheme: Boolean) =
            EstimateDialogFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_DAY_THEME_DATA, isDayTheme)
                }
            }
    }
}
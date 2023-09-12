package com.example.myapplication

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

/*open class DialogFragment(private val mainViewModel: MainViewModel) : Fragment() {
    private var default_estimate = 5
    private var currentDialog: Dialog? = null
    private var is_day_theme: Boolean  = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.estimate_dialog, container, false)
        //val is_day_theme = false // Здесь установите тему (true - дневная, false - ночная)

        arguments?.let {
            is_day_theme = it.getBoolean(is_day_theme_data)
        }

        if (is_day_theme) {
            view = inflater.inflate(R.layout.estimate_dialog, container, false)
        } else {
            view = inflater.inflate(R.layout.estimate_dialog_long_night, container, false)
        }

        val i1: ImageView = view.findViewById(R.id.star_one)
        val i2: ImageView = view.findViewById(R.id.star_two)
        val i3: ImageView = view.findViewById(R.id.star_three)
        val i4: ImageView = view.findViewById(R.id.star_four)
        val i5: ImageView = view.findViewById(R.id.star_five)
        setUpImageStar(i1)
        setUpImageStar(i2)
        setUpImageStar(i3)
        setUpImageStar(i4)
        setUpImageStar(i5)

        val cancelEstimate = view.findViewById<Button>(R.id.cancelEstimate)
        val sendEstimate = view.findViewById<Button>(R.id.sendEstimate)

        cancelEstimate.setOnClickListener { v: View? -> dismissDialog() }
        sendEstimate.setOnClickListener { v: View? -> sendEstimateClick() }

        i1.setOnClickListener { v: View? -> onStarClick(1) }
        i2.setOnClickListener { v: View? -> onStarClick(2) }
        i3.setOnClickListener { v: View? -> onStarClick(3) }
        i4.setOnClickListener { v: View? -> onStarClick(4) }
        i5.setOnClickListener { v: View? -> onStarClick(5) }

        return view
    }

    private fun setUpImageStar(imageView: ImageView) {
        imageView.setImageResource(R.drawable.channels_ic_favourite_on)
        imageView.setColorFilter(
            imageView.context.resources.getColor(R.color.colorStar),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun onStarClick(estimate: Int) {
        val stars = getBooleanFlags(estimate)
        val i1: ImageView = requireView().findViewById(R.id.star_one)
        val i2: ImageView = requireView().findViewById(R.id.star_two)
        val i3: ImageView = requireView().findViewById(R.id.star_three)
        val i4: ImageView = requireView().findViewById(R.id.star_four)
        val i5: ImageView = requireView().findViewById(R.id.star_five)
        setOnStarEstimate(i1, i2, i3, i4, i5, stars)
        default_estimate = estimate
    }

    private fun getBooleanFlags(estimate: Int): BooleanArray {
        val stars = BooleanArray(5)
        for (i in 0 until estimate) {
            stars[i] = true
        }
        return stars
    }

    private fun setOnStarEstimate(
        i1: ImageView,
        i2: ImageView,
        i3: ImageView,
        i4: ImageView,
        i5: ImageView,
        stars: BooleanArray
    ) {
        setOnImageStarEstimate(i1, stars[0])
        setOnImageStarEstimate(i2, stars[1])
        setOnImageStarEstimate(i3, stars[2])
        setOnImageStarEstimate(i4, stars[3])
        setOnImageStarEstimate(i5, stars[4])
    }

    private fun setOnImageStarEstimate(imageView: ImageView, isFullStar: Boolean) {
        if (isFullStar) {
            imageView.setImageResource(R.drawable.channels_ic_favourite_on)
        } else {
            imageView.setImageResource(R.drawable.channels_ic_favourite_off)
        }
        imageView.setColorFilter(
            imageView.context.resources.getColor(R.color.colorStar),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun dismissDialog() {
        //currentDialog?.dismiss()
        navigator().goBack()
    }

    private fun sendEstimateClick() {
        //ApplicationStatisticsReporter.sendRateAppGrade(default_estimate)
        //dismissDialog()
        if (default_estimate > 3) {
            /* mainViewModel.loadInfo(object : RequestInterface<String> {
                 override fun onSuccess(data: String) {
                     sentGoodEstimate(default_estimate.toString(), data)
                 }

                 override fun onFailure(errorData: ErrorData) {
                     sentGoodEstimate(default_estimate.toString(), "")
                 }
             })*/
            val handler = Handler()
            handler.postDelayed({
                requestPlayMarket()
                dismissDialog()
            }, 1000)
        } else {
            dismissDialog()
            //openOtherDialog(default_estimate.toString())
        }
        default_estimate = 5
    }

    /*private fun sentGoodEstimate(estimateRating: String, info: String) {
        val user_agent = UserAgent.getXLHDAgent(requireActivity())
        @SuppressLint("HardwareIds") var androidID =
            Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        if (androidID == null) {
            androidID = ""
        }
        sendEstimate(estimateRating, androidID, "", info, user_agent)
    }*/

    /*private fun sendEstimate(
        value: String,
        dvid: String,
        email: String?,
        comment: String,
        user_agent: String
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
    }*/

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

    /*@SuppressLint("HardwareIds")
    private fun openOtherDialog(estimate_rating: String) {
        try {
            val badEstimateDialog = Dialog(requireActivity())
            currentDialog = badEstimateDialog
            badEstimateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val is_day_theme = true // Здесь установите тему (true - дневная, false - ночная)

            if (is_day_theme) {
                badEstimateDialog.setContentView(R.layout.bad_estimate_dialog)
            } else {
                badEstimateDialog.setContentView(R.layout.bad_estimate_dialog_night)
            }

            val emailEstimate = badEstimateDialog.findViewById<EditText>(R.id.emailEstimate)
            val textEstimate = badEstimateDialog.findViewById<EditText>(R.id.textEstimate)
            val cancelEstimate = badEstimateDialog.findViewById<Button>(R.id.cancelEstimate)
            val sendEstimate = badEstimateDialog.findViewById<Button>(R.id.sendEstimate)

            cancelEstimate.setOnClickListener { v: View? -> dismissDialog() }
            sendEstimate.setOnClickListener { v: View? -> sendBadEstimate(emailEstimate, textEstimate) }

            badEstimateDialog.setOnDismissListener { currentDialog = null }
            badEstimateDialog.show()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }*/

    /*private fun sendBadEstimate(emailEstimate: EditText, textEstimate: EditText) {
        val estimate_rating = default_estimate.toString()
        val user_agent = UserAgent.getXLHDAgent(requireActivity())
        val stringTextEmail = emailEstimate.text.toString()
        val stringTextEstimate = textEstimate.text.toString()

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
                sendEstimate(
                    estimate_rating,
                    androidID,
                    stringTextEmail,
                    stringTextEstimate,
                    user_agent
                )
                dismissDialog()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Неверный формат EMAIL",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            var androidID =
                Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
                    ?: ""
            sendEstimate(estimate_rating, androidID, stringTextEmail, stringTextEstimate, user_agent)
            dismissDialog()
        }
    }*/

    private fun isValidEmail(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        @JvmStatic
        private val is_day_theme_data = "is_day_theme_data"

        @JvmStatic
        fun newInstance(mainViewModel: MainViewModel, is_day_theme: Boolean)=
            DialogFragment(mainViewModel).apply {
                arguments = Bundle().apply {
                    putBoolean(is_day_theme_data, is_day_theme)
                }
        }
    }

}*/
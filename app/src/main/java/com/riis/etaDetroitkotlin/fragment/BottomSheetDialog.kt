package com.riis.etaDetroitkotlin.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.riis.etaDetroitkotlin.R
import com.riis.etaDetroitkotlin.SliderAdapter

class BottomSheetDialog(private val routeGen: GeneratedRoutes) : BottomSheetDialogFragment() {

    private lateinit var imageContainer: ViewPager2
    private lateinit var dots: MutableList<TextView>


    private var layout: LinearLayout? = null

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageContainer = view.findViewById(R.id.image_container)
        layout = view.findViewById(R.id.dots_container)

        dots = mutableListOf()
        for (step in this.routeGen.legs[0].steps) dots.add(TextView(context))

        Log.i("DEBUG", dots.size.toString())


        imageContainer.adapter = SliderAdapter(routeGen)

        setIndicators()

        imageContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedDots(position)
                super.onPageSelected(position)
            }
        })
    }

    private fun selectedDots(position: Int) {
        for (i in dots.indices) {
            if (i == position) {
                dots[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            } else {
                dots[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
        }
    }

    private fun setIndicators() {
        for (i in dots.indices) {
            dots[i] = TextView(context)
            dots[i].text = HtmlCompat.fromHtml("&#9679;", HtmlCompat.FROM_HTML_MODE_LEGACY)
            dots[i].textSize = 18F
            layout?.addView(dots[i])
        }
    }
}
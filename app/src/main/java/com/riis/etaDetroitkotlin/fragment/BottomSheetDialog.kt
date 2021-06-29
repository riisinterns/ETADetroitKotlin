package com.riis.etaDetroitkotlin.fragment

import com.riis.etaDetroitkotlin.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog : BottomSheetDialogFragment() {

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.bottom_sheet_layout, container, false)
        val button1 = v.findViewById<Button>(R.id.button1)
        val button2 = v.findViewById<Button>(R.id.button2)
        return v
    }

}
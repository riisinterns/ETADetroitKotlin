package com.riis.etaDetroitkotlin.fragment

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.riis.etaDetroitkotlin.R

class RouteLoadingDialog(private val activity: Activity) {

    private lateinit var dialog: AlertDialog
    private lateinit var routeText: TextView

    fun startLoadingDialog(route: String) {
        val builder = AlertDialog.Builder(activity)
        var inflater: LayoutInflater = activity.layoutInflater

        var myView = inflater.inflate(R.layout.loading_dialog, null)

        builder.setView(myView)
        builder.setCancelable(false)
        dialog = builder.create()

        routeText = myView.findViewById(R.id.textView)
        routeText.text = "Loading $route..."

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

}
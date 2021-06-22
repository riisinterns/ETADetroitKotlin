package com.riis.etaDetroitkotlin.fragment

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.riis.etaDetroitkotlin.R


class RouteLoadingDialog: DialogFragment() {

    //CLASS VARIABLES
    //---------------
    private val safeArgs: RouteLoadingDialogArgs by navArgs() //gaining type-safe access to the arguments sent to TimePickerFragment.kt
    private lateinit var routeText: TextView

    //CREATING THE DIALOG
    //---------------------
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val route = safeArgs.route

        val builder = AlertDialog.Builder(requireActivity())

        val inflater: LayoutInflater? = activity?.layoutInflater

        val myView = inflater?.inflate(R.layout.loading_dialog, null)

        builder.setView(myView)
        builder.setCancelable(false)
        val dialog = builder.create()

        if (myView != null) {
            routeText = myView.findViewById(R.id.textView)
        }

        routeText.text = "Loading $route..."

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }


}



/*
    private lateinit var dialog: AlertDialog


    fun startLoadingDialog(route: String) {
        val builder = AlertDialog.Builder(requireActivity())
        var inflater: LayoutInflater? = activity?.layoutInflater

        var myView = inflater?.inflate(R.layout.loading_dialog, null)

        builder.setView(myView)
        builder.setCancelable(false)
        dialog = builder.create()

        if (myView != null) {
            routeText = myView.findViewById(R.id.textView)
        }
        routeText.text = "Loading $route..."

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }

     */
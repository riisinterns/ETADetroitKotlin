package com.riis.etaDetroitkotlin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.riis.etaDetroitkotlin.R
import com.riis.etaDetroitkotlin.databinding.LoadingDialogBinding
import java.util.*

private const val ARG_ROUTE = "route"

class RouteLoadingDialog(val route:String): DialogFragment() {

    private var _binding: LoadingDialogBinding? = null
    private val binding get() = _binding!!


    //CREATING THE DIALOG
    //---------------------
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = LoadingDialogBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        builder.setCancelable(false)


        val dialog = builder.create()

        binding.textView.text = "Loading $route..."

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    companion object {
        fun newInstance(route: String): RouteLoadingDialog {
            val args = Bundle().apply {
                putSerializable(ARG_ROUTE, route)
            }
            return RouteLoadingDialog(route).apply {
                arguments = args
            }
        }
    }


     */


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
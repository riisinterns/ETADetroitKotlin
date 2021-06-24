package com.riis.etaDetroitkotlin.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.riis.etaDetroitkotlin.databinding.LoadingDialogBinding

class RouteLoadingDialog(val route: String) : DialogFragment() {

    private var _binding: LoadingDialogBinding? = null
    private val binding get() = _binding!!


    //CREATING THE DIALOG
    //---------------------
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = LoadingDialogBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        builder.setCancelable(false)


        val dialog: Dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)

        binding.textView.text = "Loading $route..."

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
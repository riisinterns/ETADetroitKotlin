package com.riis.etaDetroitkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.riis.etaDetroitkotlin.R


class RoutesFragment : Fragment() {

    //CREATING THE FRAGMENT VIEW
    //--------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the fragment_transport_list layout as the fragment view
        val view = inflater.inflate(R.layout.fragment_routes, container, false)
        return view
    }
}
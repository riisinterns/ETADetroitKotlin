package com.riis.etaDetroitkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.riis.etaDetroitkotlin.model.Company
import androidx.fragment.app.Fragment

class RoutesFragment : Fragment() {

    //CLASS VARIABLES
    //---------------
    private lateinit var idTextView : TextView
    private lateinit var nameTextView : TextView
    private lateinit var colorTextView : TextView
    private lateinit var busImgUrlTextView : TextView

    //LINKING FRAGMENT WITH VIEW MODEL
    //--------------------------------
    private val homeToRoutesSharedViewModel: HomeToRoutesSharedViewModel by activityViewModels()

    //CREATING THE FRAGMENT VIEW
    //--------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the fragment_transport_list layout as the fragment view
        val view = inflater.inflate(R.layout.fragment_routes, container, false)

        //referencing the Views from the fragment_routes layout
        idTextView = view.findViewById(R.id.id_text_view)
        nameTextView = view.findViewById(R.id.name_text_view)
        colorTextView = view.findViewById(R.id.color_text_view)
        busImgUrlTextView = view.findViewById(R.id.busImgUrl_text_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeToRoutesSharedViewModel.companyContainer.observe(viewLifecycleOwner, { selectedCompany ->
            idTextView.text = selectedCompany.id.toString()
            nameTextView.text = selectedCompany.name
            colorTextView.text = selectedCompany.brandColor
            busImgUrlTextView.text = selectedCompany.busImgUrl

        })
    }
}
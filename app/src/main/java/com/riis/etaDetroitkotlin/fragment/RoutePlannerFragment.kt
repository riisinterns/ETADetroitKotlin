package com.riis.etaDetroitkotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.riis.etaDetroitkotlin.R
import com.riis.etaDetroitkotlin.SharedViewModel
import com.riis.etaDetroitkotlin.model.Company

class RoutePlannerFragment : Fragment() {

    private lateinit var listOfCompanies: List<Company>
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route_planner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.companyListLiveData.observe(
            viewLifecycleOwner,
            { companyList ->
                companyList?.let {
                    listOfCompanies = companyList
                }
            }
        )

    }


}
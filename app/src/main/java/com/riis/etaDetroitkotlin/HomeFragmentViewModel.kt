package com.riis.etaDetroitkotlin

import androidx.lifecycle.ViewModel
import com.riis.etaDetroitkotlin.model.Company

//TransportListViewModel is a ViewModel used to store a list of Transport objects for CompanyListFragment.kt
class HomeFragmentViewModel : ViewModel() {

    val companyList = mutableListOf<Company>()
    private val companyNames = ArrayList<String>(listOf("SmartBus", "DDOT", "RefleX", "People Mover", "QLine", "Route Map"))

    init{
        for (name in companyNames){
            val company = Company()
            company.name = name
            companyList += company
        }
    }

}
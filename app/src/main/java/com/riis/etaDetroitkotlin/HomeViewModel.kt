package com.riis.etaDetroitkotlin

import androidx.lifecycle.ViewModel
import com.riis.etaDetroitkotlin.database.BusRepository


class HomeViewModel : ViewModel() {

    private val busRepository = BusRepository.get()
    val companyListLiveData = busRepository.getCompanies()

//    private val companyNames = ArrayList<String>(listOf("SmartBus", "DDOT", "RefleX", "People Mover", "QLine", "Route Map"))
//
//    init{
//        for (name in companyNames){
//            val company = Company()
//            company.name = name //todo change back to val once database is fully implemented
//            companyList += company
//        }
//    }

}
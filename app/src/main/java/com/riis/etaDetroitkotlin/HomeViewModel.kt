package com.riis.etaDetroitkotlin

import androidx.lifecycle.ViewModel
import com.riis.etaDetroitkotlin.database.BusRepository
import com.riis.etaDetroitkotlin.model.Company


class HomeViewModel : ViewModel() {

    private val busRepository = BusRepository.get()
    val companyListLiveData = busRepository.getCompanies()

    private val companyList = mutableListOf<Company>()
    private val companyIDs = ArrayList<Int>(listOf(1, 2, 3, 4, 5, 6))
    private val companyNames = ArrayList<String>(listOf("SmartBus", "DDOT", "RefleX", "People Mover", "QLine", "Route Map"))
    private val companyBrandColors = ArrayList<String>(listOf("#BC0E29", "#054839", "#5092fc", "#21487A", "#973c37", "#009C77"))
    private val companyBusImgUrls = ArrayList<String>(listOf("smart", "ddot_bus", "reflex", "people_mover", "qline", "route_map_card_image"))

    init{
        for (i in 0..5){
            val company = Company()
            company.id = companyIDs[i]
            company.name = companyNames[i]
            company.brandColor = companyBrandColors[i]
            company.busImgUrl = companyBusImgUrls[i]
            companyList += company
        }


    }

}
package com.riis.etaDetroitkotlin

import androidx.lifecycle.ViewModel
import com.riis.etaDetroitkotlin.database.BusRepository
import com.riis.etaDetroitkotlin.model.Company


class HomeViewModel : ViewModel() {

    private val busRepository = BusRepository.get()
    val companyListLiveData = busRepository.getCompanies()

}
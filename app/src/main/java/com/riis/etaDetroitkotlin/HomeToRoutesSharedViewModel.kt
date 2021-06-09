package com.riis.etaDetroitkotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.riis.etaDetroitkotlin.database.BusDatabase
import com.riis.etaDetroitkotlin.database.BusRepository
import com.riis.etaDetroitkotlin.model.Company
import com.riis.etaDetroitkotlin.model.Routes

//ViewModel class allows HomeFragment.kt to send data to RoutesFragment.kt
class HomeToRoutesSharedViewModel : ViewModel() {

    private val busRepository = BusRepository.get()
    private val companyContainer =
        MutableLiveData<Company>() //this variable can store a Company object and is wrapped in LiveData

    // ... to allow observers to listen to any changes to it

    var routeListLiveData: LiveData<List<Routes>> =
        Transformations.switchMap(companyContainer){ company ->
            busRepository.getRoutes(company.id)
        }

    fun saveCompany(newCompany: Company) { //this function sets the value of companyContainer to a new Company object
        companyContainer.value = newCompany
    }


}
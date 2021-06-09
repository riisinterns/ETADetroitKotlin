package com.riis.etaDetroitkotlin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riis.etaDetroitkotlin.model.Company

//ViewModel class allows HomeFragment.kt to send data to RoutesFragment.kt
class HomeToRoutesSharedViewModel : ViewModel() {

    val companyContainer = MutableLiveData<Company>() //this variable can store a Company object and is wrapped in LiveData
                                             // ... to allow observers to listen to any changes to it

    fun saveCompany(newCompany: Company) { //this function sets the value of companyContainer to a new Company object
        companyContainer.value = newCompany
    }
}
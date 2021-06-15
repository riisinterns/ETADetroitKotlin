package com.riis.etaDetroitkotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.riis.etaDetroitkotlin.database.BusRepository
import com.riis.etaDetroitkotlin.model.*

class SharedViewModel : ViewModel() {

    private val busRepository = BusRepository.get()
    val companyListLiveData = busRepository.getCompanies()

    private val companyContainer =
        MutableLiveData<Company>() //this variable can store a Company object and is wrapped in LiveData
    private val routeContainer = MutableLiveData<Routes>()

    // ... to allow observers to listen to any changes to it

    var routeListLiveData: LiveData<List<Routes>> =
        //switches the Routes (observed in RoutesFragment) based of the company that gets saved
        Transformations.switchMap(companyContainer) { company ->
            busRepository.getRoutes(company.id)
        }

    var routeStopsListLiveData: LiveData<List<RouteStops>> =
        //switches the RouteStops (observed in StopFragment) based of the Route that gets saved
        Transformations.switchMap(routeContainer) { route ->
            busRepository.getRouteStops(route.id)
        }

    fun getTripStops(stopId: Int): LiveData<List<TripStops>> {
        return busRepository.getTripStops(stopId)
    }

    var daysOfOperationLiveData: LiveData<List<DaysOfOperation>> = busRepository.getDays()

    val currentCompany: Company?
        get() = companyContainer.value

    val currentRoute: Routes?
        get() = routeContainer.value

    fun saveCompany(newCompany: Company) { //this function sets the value of companyContainer to a new Company object
        companyContainer.value = newCompany
    }

    fun saveRoute(newRoute: Routes) {
        routeContainer.value = newRoute
    }

    fun getStopLiveData(stopId: Int): LiveData<Stops> {
        return busRepository.getStop(stopId)
    }
}
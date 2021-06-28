package com.riis.etaDetroitkotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.riis.etaDetroitkotlin.database.BusRepository
import com.riis.etaDetroitkotlin.model.Company
import com.riis.etaDetroitkotlin.model.RouteStopInfo
import com.riis.etaDetroitkotlin.model.Routes
import com.riis.etaDetroitkotlin.model.TripStops

class SharedViewModel : ViewModel() {

    private val busRepository = BusRepository.get()
    val companyListLiveData = busRepository.getCompanies()

    var hasEntered = false

    var direction = 0
    var directionCount: Int = 0
    var currentMarkerLatitude = 0.0
    var currentMarkerLongitude = 0.0

    //this variable can store a Company object and is wrapped in LiveData
    private val companyContainer = MutableLiveData<Company>()
    private val routeContainer = MutableLiveData<Routes>()

    // ... to allow observers to listen to any changes to it

    var routeListLiveData: LiveData<List<Routes>> =
        //switches the Routes (observed in RoutesFragment) based of the company that gets saved
        Transformations.switchMap(companyContainer) { company ->
            busRepository.getRoutes(company.id)
        }

    var routeStopsInfoListLiveData: LiveData<List<RouteStopInfo>> =
        //switches the RouteStopsInfo (observed in StopFragmentChild) based of the Route that gets saved
        Transformations.switchMap(routeContainer) { route ->
            busRepository.getStopsInfoOnRoute(route.id)
        }

    fun getArrivalTimes(
        routeId: Int,
        directionId: Int,
        dayId: Int,
        stopId: Int
    ): LiveData<List<TripStops>> {
        return busRepository.getArrivalTimes(routeId, directionId, dayId, stopId)
    }

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
}
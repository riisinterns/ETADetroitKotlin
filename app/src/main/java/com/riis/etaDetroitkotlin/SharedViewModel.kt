package com.riis.etaDetroitkotlin

import androidx.lifecycle.*
import com.riis.etaDetroitkotlin.database.BusRepository
import com.riis.etaDetroitkotlin.fragment.DirectionResponse
import com.riis.etaDetroitkotlin.model.*

class SharedViewModel : ViewModel() {

    private val busRepository = BusRepository.get()
    val companyListLiveData = busRepository.getCompanies()

    var hasEntered = false

    var direction = 0
    var directionCount: Int = 0
    var currentMarkerLatitude = 0.0
    var currentMarkerLongitude = 0.0

    //each variable can store a class object and is wrapped in LiveData
    // ... to allow observers to listen to any changes to it
    private val companyContainer = MutableLiveData<Company>()
    private val routeContainer = MutableLiveData<Routes>()
    private val directionResponseContainer = MutableLiveData<DirectionResponse?>()

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

    var tripDaysOfOperationContainer = listOf<TripDaysOfOperation>()

    fun getArrivalTimes(
        routeId: Int,
        directionId: Int,
        dayId: Int,
        stopId: Int,
    ): LiveData<List<TripStops>> {
        return busRepository.getArrivalTimes(routeId, directionId, dayId, stopId)
    }

    fun getNewTripStops(stopId: Int): LiveData<List<TripStops>> {
        return busRepository.getNewTripStops(stopId)
    }

    fun getTrips(routeId: Int, directionId: Int): LiveData<List<Trips>> {
        return busRepository.getTrips(routeId, directionId)
    }

    fun getTripDaysOfOperation(): LiveData<List<TripDaysOfOperation>> {
        return busRepository.getTripDaysOfOperation()
    }


    val currentCompany: Company?
        get() = companyContainer.value

    val currentRoute: Routes?
        get() = routeContainer.value

    val currentDirectionResponse: DirectionResponse?
        get() = directionResponseContainer.value

    fun saveCompany(newCompany: Company) { //this function sets the value of companyContainer to a new Company object
        companyContainer.value = newCompany
    }

    fun saveRoute(newRoute: Routes) {
        routeContainer.value = newRoute
    }

    fun saveDirectionResponse(newDirectionResponse: DirectionResponse) {
        directionResponseContainer.value = newDirectionResponse
    }

    fun clearDirectionResponse() {
        directionResponseContainer.value = null
    }
}
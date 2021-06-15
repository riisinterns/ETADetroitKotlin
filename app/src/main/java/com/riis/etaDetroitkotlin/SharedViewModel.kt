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

    //this variable can store a Company object and is wrapped in LiveData to allow observers to listen to any changes to it
    private val companyContainer = MutableLiveData<Company>()
    private val routeContainer = MutableLiveData<Routes>()
    private val stopContainer = MutableLiveData<Stops>()


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

    var tripStopsListLiveData: LiveData<List<TripStops>> =
        //switches the TripStops (observed in StopFragment) based of the Stop that gets saved
        Transformations.switchMap(stopContainer) { stop ->
            busRepository.getTripStops(stop.id)
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

    fun saveStop(newStop: Stops) {
        stopContainer.value = newStop
    }


    fun getStopLiveData(stopId: Int): LiveData<Stops> {
        return busRepository.getStop(stopId)
    }


}
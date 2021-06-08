package com.riis.etaDetroitkotlin

import androidx.lifecycle.ViewModel

//TransportListViewModel is a ViewModel used to store a list of Transport objects for TransportListFragment.kt
class TransportListViewModel : ViewModel() {

    val transportList = mutableListOf<Transport>()
    val transportNames = ArrayList<String>(listOf("SmartBus", "DDOT", "RefleX", "People Mover", "QLine", "Route Map"))

    init{
        for (name in transportNames){
            val transport = Transport()
            transport.name = name
            transportList += transport
        }
    }

}
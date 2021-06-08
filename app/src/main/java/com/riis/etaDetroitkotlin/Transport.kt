package com.riis.etaDetroitkotlin

import java.util.*

//CREATING THE MODEL LAYER FOR THE APP

//the Transport data class creates objects which represent different modes of transportation
data class Transport(val id: UUID = UUID.randomUUID(), //random unique id
                  var name: String = "",
                  var imageId: String = "") //image id from Resources (res/drawable)
package com.riis.etaDetroitkotlin

import com.riis.etaDetroitkotlin.model.Stops

data class StopContainer (
    var stop: Stops,
    var hidden: Boolean = true
)
package com.dr.fintracker.data.entities

import java.util.*

data class EmissionData(
    var direction : EmissionDirection,
    var startDate : Date,
    var value : Int = 0
)
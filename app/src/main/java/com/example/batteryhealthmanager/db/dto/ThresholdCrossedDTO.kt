package com.example.batteryhealthmanager.db.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ThresholdCrossedDTO(
    var crossedLowerBound: Boolean,
    var crossedUpperBound: Boolean
) : Parcelable
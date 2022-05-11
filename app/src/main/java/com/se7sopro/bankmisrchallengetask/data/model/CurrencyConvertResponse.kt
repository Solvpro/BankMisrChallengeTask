package com.se7sopro.bankmisrchallengetask.data.model

import android.icu.text.IDNA

data class CurrencyConvertResponse(
    var date: String? = null,
    var historical: Boolean? = null,
    var info: InfoModel? = null,
    var result: Double? = null,
    var success: Boolean? = null,
)

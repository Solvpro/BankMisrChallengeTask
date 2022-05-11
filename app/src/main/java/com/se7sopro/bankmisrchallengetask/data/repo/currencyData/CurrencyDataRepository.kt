package com.se7sopro.bankmisrchallengetask.data.repo.currencyData

import com.se7sopro.bankmisrchallengetask.data.model.CurrencyConvertResponse
import com.se7sopro.bankmisrchallengetask.data.model.SymbolsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface CurrencyDataRepository {

    suspend fun getSymbols(): Flow<Response<SymbolsResponse>>

    suspend fun convertCurrency(
        amount: String,
        from: String,
        to: String,
        date: String
    ): Flow<Response<CurrencyConvertResponse>>
}
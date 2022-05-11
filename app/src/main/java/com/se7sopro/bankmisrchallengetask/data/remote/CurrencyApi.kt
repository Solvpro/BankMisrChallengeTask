package com.se7sopro.bankmisrchallengetask.data.remote

import com.se7sopro.bankmisrchallengetask.data.model.CurrencyConvertResponse
import com.se7sopro.bankmisrchallengetask.data.model.SymbolsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("symbols")
    suspend fun getSymbols(): Response<SymbolsResponse>

    @GET("convert")
    suspend fun convertCurrency(
        @Query("amount") amount: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("date") date: String
    ): Response<CurrencyConvertResponse>

}
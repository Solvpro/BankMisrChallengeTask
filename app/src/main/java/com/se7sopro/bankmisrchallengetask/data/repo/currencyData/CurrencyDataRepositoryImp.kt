package com.se7sopro.bankmisrchallengetask.data.repo.currencyData

import com.se7sopro.bankmisrchallengetask.data.model.CurrencyConvertResponse
import com.se7sopro.bankmisrchallengetask.data.model.SymbolsResponse
import com.se7sopro.bankmisrchallengetask.data.remote.CurrencyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class CurrencyDataRepositoryImp @Inject constructor(private val api: CurrencyApi) :
    CurrencyDataRepository {

    override suspend fun getSymbols(): Flow<Response<SymbolsResponse>> {
        return flow {
            emit(
                api.getSymbols()
            )
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun convertCurrency(
        amount: String,
        from: String,
        to: String,
        date: String
    ): Flow<Response<CurrencyConvertResponse>> {
        return flow {
            emit(
                api.convertCurrency(amount, from, to, date)
            )
        }.flowOn(Dispatchers.IO)
    }
}
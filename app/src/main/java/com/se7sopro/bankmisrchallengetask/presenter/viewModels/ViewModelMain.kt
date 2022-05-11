package com.se7sopro.bankmisrchallengetask.presenter.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.se7sopro.bankmisrchallengetask.data.remote.ViewState
import com.se7sopro.bankmisrchallengetask.data.repo.currencyData.CurrencyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMain @Inject constructor(
    private val currencyDataRepository: CurrencyDataRepository
) : ViewModel() {
    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val _getCurrencyDataStateFlow =
        MutableLiveData<ViewState<Any>>()
    val getCurrencyDataStateFlow: MutableLiveData<ViewState<Any>>
        get() = _getCurrencyDataStateFlow

    private var _fromCurrencyDataStateFlow : Pair<String, Int> = Pair("From", 0)

    private var _toCurrencyDataStateFlow : Pair<String, Int> = Pair("To", 0)

    fun saveFromCurrency(fromCurrency: Pair<String, Int>) {
        _fromCurrencyDataStateFlow = fromCurrency
    }

    fun saveToCurrency(toCurrency: Pair<String, Int>) {
        _toCurrencyDataStateFlow = toCurrency
    }

    fun getFromCurrency(): Pair<String, Int> {
       return _fromCurrencyDataStateFlow
    }

    fun getToCurrency(): Pair<String, Int> {
       return _toCurrencyDataStateFlow
    }

    fun getSymbols() {
        scope.launch {
            _getCurrencyDataStateFlow.postValue(ViewState.Loading(true))
            currencyDataRepository.getSymbols()
                .catch {
                    _getCurrencyDataStateFlow.postValue(ViewState.Loading(false))

                    it.message?.let {
                        _getCurrencyDataStateFlow.postValue(
                            ViewState.GeneralError(0, it)
                        )
                    }

                }.buffer()
                .collect {
                    _getCurrencyDataStateFlow.postValue(ViewState.Loading(false))
                    if (it.isSuccessful) {
                        _getCurrencyDataStateFlow.postValue(ViewState.Success(it.body()!!))
                    } else
                        _getCurrencyDataStateFlow.postValue(
                            ViewState.DataError(
                                it.code(),
                                it.errorBody()?.string()!!
                            )
                        )
                }
        }
    }

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String,
        date: String
    ) {
        scope.launch {
            _getCurrencyDataStateFlow.postValue(ViewState.Loading(true))
            currencyDataRepository.convertCurrency(amountStr, fromCurrency, toCurrency, date)
                .catch {
                    _getCurrencyDataStateFlow.postValue(ViewState.Loading(false))

                    it.message?.let {
                        _getCurrencyDataStateFlow.postValue(
                            ViewState.GeneralError(0, it)
                        )
                    }

                }.buffer()
                .collect {
                    _getCurrencyDataStateFlow.postValue(ViewState.Loading(false))
                    if (it.isSuccessful) {
                        _getCurrencyDataStateFlow.postValue(ViewState.Success(it.body()!!))
                    } else
                        _getCurrencyDataStateFlow.postValue(
                            ViewState.DataError(
                                it.code(),
                                it.errorBody()?.string()!!
                            )
                        )
                }
        }
    }

}
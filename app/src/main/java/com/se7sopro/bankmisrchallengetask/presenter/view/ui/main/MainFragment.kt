package com.se7sopro.bankmisrchallengetask.presenter.view.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.textChanges
import com.se7sopro.bankmisrchallengetask.R
import com.se7sopro.bankmisrchallengetask.data.extention.getMap
import com.se7sopro.bankmisrchallengetask.data.model.*
import com.se7sopro.bankmisrchallengetask.data.remote.ViewState
import com.se7sopro.bankmisrchallengetask.databinding.FragmentMainBinding
import com.se7sopro.bankmisrchallengetask.presenter.view.ui.adapter.SpinnerSelectableItemAdapter
import com.se7sopro.bankmisrchallengetask.presenter.viewModels.ViewModelMain
import com.se7sopro.bankmisrchallengetask.utils.DateUtils
import com.se7sopro.bankmisrchallengetask.utils.listeners.SpinnerItemSelected
import com.se7sopro.bankmisrchallengetask.utils.listeners.SpinnerItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: ViewModelMain by viewModels()
    private lateinit var binding: FragmentMainBinding
    var symbolList = ArrayList<SymbolModel>()

    private lateinit var fromCurrencySpinnerAdapter: SpinnerSelectableItemAdapter
    private lateinit var toCurrencySpinnerAdapter: SpinnerSelectableItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        initUi()
        return binding.root
    }

    private fun initUi() {
        binding.ibChangeCurrency.setOnClickListener {
            val from = viewModel.getFromCurrency()
            viewModel.saveFromCurrency(viewModel.getToCurrency())
            viewModel.saveToCurrency(from)
            binding.spFromCurrency.setSelection(viewModel.getFromCurrency().second + 1)
            binding.spToCurrency.setSelection(viewModel.getToCurrency().second + 1)
            changeAmount(binding.etCurrencyAmount.text.toString())
        }
        binding.spFromCurrency.onItemSelectedListener =
            SpinnerItemSelected(fromCurrencySpinnerSelected)
        binding.spToCurrency.onItemSelectedListener =
            SpinnerItemSelected(toCurrencySpinnerSelected)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    private fun setUp() {
        viewModel.getSymbols()
        viewModel.getCurrencyDataStateFlow.observe(this, { state ->
            when (state) {
                is ViewState.Loading -> {
                    showLoading(state.isVisible)
                }
                is ViewState.Success -> {
                    showLoading(false)
                    when (state.data) {
                        is CurrencyConvertResponse -> {
                            val response = state.data
                            binding.etCurrencyConvert.setText(response.result.toString())
                        }
                        is SymbolsResponse -> {
                            handleSymbolsResponse(state.data)
                        }
                    }
                }
                is ViewState.DataError -> {
                    showLoading(false)
                    Log.e("Map", "key: error - value: ${state.data as String} \n")
                    handleErrorResponse(state.data)
                }
                is ViewState.GeneralError -> {
                    showLoading(false)
                    Log.e("Map", "key: error - value: ${state.data.toString()} \n")
                    showErrorMessage(state.data.toString())
                }
            }
        })

    }

    private fun showLoading(visible: Boolean) {
        binding.progressBar.isVisible = visible
    }

    private fun handleErrorResponse(errorResponse: String) {
        Toast.makeText(context, errorResponse, Toast.LENGTH_LONG).show()
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun handleSymbolsResponse(response: SymbolsResponse) {
        if (response.success!!) {
            val map = getMap(response.symbols)
            addListAndNotifyData(map)
        }
    }

    private fun addListAndNotifyData(map: Map<String, Any>?) {
        var position = 0
        map?.forEach {
            symbolList.add(SymbolModel(it.key, it.value as String, position++))
        }
        showFromCurrencySpinner(symbolList)
        showToCurrencySpinner(symbolList)
        searchOutLetSetUp()
    }

    private fun showFromCurrencySpinner(currencies: List<SymbolModel>) {
        fromCurrencySpinnerAdapter =
            SpinnerSelectableItemAdapter(
                requireContext(),
                currencies.toMutableList(),
                R.string.from
            )
        binding.spFromCurrency.adapter = fromCurrencySpinnerAdapter
    }

    private fun showToCurrencySpinner(currencies: List<SymbolModel>) {
        toCurrencySpinnerAdapter =
            SpinnerSelectableItemAdapter(
                requireContext(),
                currencies.toMutableList(),
                R.string.to
            )
        binding.spToCurrency.adapter = toCurrencySpinnerAdapter
    }

    private fun searchOutLetSetUp() {
        binding.etCurrencyAmount.textChanges()
            .skipInitialValue()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotBlank()) {
                    changeAmount(it.toString())
                } else {
                    changeAmount((1).toString())
                }
            }
    }

    private fun changeAmount(currency: String) {
        viewModel.convert(
            currency,
            viewModel.getFromCurrency().first,
            viewModel.getToCurrency().first,
            DateUtils.getCurrentDate(),
        )
    }

    private val fromCurrencySpinnerSelected = object : SpinnerItemSelectedListener {
        override fun onItemSelectedListener(position: Int) {
            if (position == 0) return
            val fromCurrencyModel = fromCurrencySpinnerAdapter.getItem(position)
            fromCurrencyModel?.let {
                it as SymbolModel
                viewModel.saveFromCurrency(Pair(it.name, it.id!!))
            }
        }
    }

    private val toCurrencySpinnerSelected = object : SpinnerItemSelectedListener {
        override fun onItemSelectedListener(position: Int) {
            if (position == 0) return
            val toCurrencyModel = toCurrencySpinnerAdapter.getItem(position)
            toCurrencyModel?.let {
                it as SymbolModel
                viewModel.saveToCurrency(Pair(it.name, it.id!!))
            }
        }
    }

}
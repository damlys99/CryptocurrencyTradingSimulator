package com.example.cryptocurrencytradingsimulator.viewmodels

import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytradingsimulator.MainApplication
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.Repository
import com.example.cryptocurrencytradingsimulator.data.api.ApiRepository
import com.example.cryptocurrencytradingsimulator.data.models.Crypto
import com.example.cryptocurrencytradingsimulator.data.models.CryptoChartData
import com.example.cryptocurrencytradingsimulator.data.models.Transaction
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.internal.aggregatedroot.codegen._com_example_cryptocurrencytradingsimulator_MainApplication
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

class CryptoViewModel @AssistedInject constructor(
    private val repository: ApiRepository,
    @Assisted private val cryptoId: String
): BaseViewModel() {
    val crypto: MutableLiveData<Crypto> = MutableLiveData()
    val chartData: MutableLiveData<CryptoChartData> = MutableLiveData()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()

    init{
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", exception.toString())
        }) {
            withContext(Dispatchers.IO) {
                crypto.postValue(repository.getCrypto(cryptoId)[0])
            }
        }
        getChartData("1D")
        getTransactions()
    }

    companion object {
        fun provideFactory(
            assistedFactory:CryptoViewModelFactory,
            cryptoId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(cryptoId) as T
            }
        }
    }

    fun getChartData(interval: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val toLDT = LocalDateTime.now()
                lateinit var fromLDT: LocalDateTime
                when (interval) {
                    MainApplication.applicationContext()
                        .getString(R.string.chart_radio_label_1d) -> fromLDT = toLDT.minusDays(1)
                    MainApplication.applicationContext()
                        .getString(R.string.chart_radio_label_7d) -> fromLDT = toLDT.minusWeeks(1)
                    MainApplication.applicationContext()
                        .getString(R.string.chart_radio_label_14d) -> fromLDT = toLDT.minusWeeks(2)
                    MainApplication.applicationContext()
                        .getString(R.string.chart_radio_label_1m) -> fromLDT = toLDT.minusMonths(1)
                    MainApplication.applicationContext()
                        .getString(R.string.chart_radio_label_3m) -> fromLDT =
                        toLDT.minusMonths(3).minusDays(2)
                    MainApplication.applicationContext()
                        .getString(R.string.chart_radio_label_6m) -> fromLDT = toLDT.minusMonths(6)
                    MainApplication.applicationContext()
                        .getString(R.string.chart_radio_label_1y) -> fromLDT = toLDT.minusYears(1)
                    "else" -> fromLDT = toLDT
                }
                Log.d(
                    fromLDT.atZone(ZoneId.systemDefault()).toEpochSecond().toString(), toLDT.atZone(
                        ZoneId.systemDefault()
                    ).toEpochSecond().toString()
                )
                chartData.postValue(repository.getHistoricalData(
                    cryptoId,
                    fromLDT.atZone(ZoneId.systemDefault()).toEpochSecond(),
                    toLDT.atZone(
                        ZoneId.systemDefault()
                    ).toEpochSecond()
                ))
            }
        }
    }

    fun getTransactions() {
        GlobalScope.async {
            transactions.postValue(listOf(Transaction(0, "", 0.0, 0.0, 0.0 ,0.0,0, 0)) + repository.getTransactions(cryptoId))
            Log.e("ID", cryptoId)
        }
    }

}
@AssistedFactory
interface CryptoViewModelFactory {
    fun create(cryptoId: String): CryptoViewModel

}
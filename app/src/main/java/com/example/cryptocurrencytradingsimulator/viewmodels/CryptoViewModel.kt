package com.example.cryptocurrencytradingsimulator.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencytradingsimulator.MainApplication
import com.example.cryptocurrencytradingsimulator.R
import com.example.cryptocurrencytradingsimulator.data.api.Repository
import com.example.cryptocurrencytradingsimulator.data.models.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class CryptoViewModel @AssistedInject constructor(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences,
    @Assisted private val cryptoId: String
) : BaseViewModel() {
    val crypto: MutableLiveData<Crypto> = MutableLiveData()
    val chartData: MutableLiveData<CryptoChartData> = MutableLiveData()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()
    var money: MutableLiveData<Float> = MutableLiveData(sharedPreferences.getFloat("money", 0F))
    val owned: MutableLiveData<Owned> = MutableLiveData()

    init {
        viewModelScope.launch(CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", exception.toString())
        }) {
            withContext(Dispatchers.IO) {
                crypto.postValue(repository.getCrypto(cryptoId)[0])
            }
        }
        getChartData("1D")
        getTransactions()
        getOwned()
    }

    companion object {
        fun provideFactory(
            assistedFactory: CryptoViewModelFactory,
            cryptoId: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(cryptoId) as T
            }
        }
    }

    fun getOwned() {
        GlobalScope.async {
                owned.postValue(repository.getOwned(cryptoId))
            }
    }

    fun getChartData(interval: String) {
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

                chartData.postValue(
                    repository.getHistoricalData(
                        cryptoId,
                        fromLDT.atZone(ZoneId.systemDefault()).toEpochSecond(),
                        toLDT.atZone(
                            ZoneId.systemDefault()
                        ).toEpochSecond()
                    )
                )
            }
        }
    }

    fun getTransactions() {
        GlobalScope.async {
            transactions.postValue(repository.getTransactions(cryptoId))
        }
    }

    fun buyCrypto(value: Double, amount: Double) {
        GlobalScope.async {
            val ownd = repository.getOwned(cryptoId)
            if (ownd != null) {
                ownd.amount += amount
                repository.ownedDao.update(ownd)
            } else {
                repository.ownedDao.insert(Owned(cryptoId = cryptoId, cryptoName = crypto.value!!.name!!, amount = amount))
            }
            val newMoney = (money.value!! - value).toFloat()
            owned.postValue(repository.getOwned(cryptoId))
            sharedPreferences.edit().putFloat("money", newMoney).apply()
            money.postValue(sharedPreferences.getFloat ("money", 0F))
            repository.transactionDao.insert(
                Transaction(
                    type = TransactionType.BUY,
                    cryptoId = cryptoId,
                    income = -value,
                    quantity = amount,
                    price = crypto.value!!.current_price!!,
                    balance = newMoney.toDouble(),
                    owned = owned.value!!.amount,
                    date = Instant.now().epochSecond
                )
            )
            getTransactions()
            transactions.postValue(transactions.value)
        }


    }
    fun sellCrypto(value: Double, amount: Double) {
        GlobalScope.async {
            val ownd = repository.getOwned(cryptoId)
            if (ownd != null) {
                ownd.amount -= amount
                if(ownd.amount != 0.0)  repository.ownedDao.update(ownd)
                else repository.ownedDao.delete(ownd)
            }
            getOwned()
            val newMoney = (money.value!! + value).toFloat()
            sharedPreferences.edit().putFloat("money", newMoney).apply()
            money.postValue(sharedPreferences.getFloat("money", 0F))
            repository.transactionDao.insert(
                Transaction(
                    type = TransactionType.SELL,
                    cryptoId = cryptoId,
                    income = value,
                    quantity = amount,
                    price = crypto.value!!.current_price!!,
                    balance = newMoney.toDouble(),
                    owned = owned.value!!.amount,
                    date = Instant.now().epochSecond
                )
            )
            getTransactions()
            transactions.postValue(transactions.value)
        }


    }

}

@AssistedFactory
interface CryptoViewModelFactory {
    fun create(cryptoId: String): CryptoViewModel

}
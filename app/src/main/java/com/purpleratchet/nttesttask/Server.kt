package com.purpleratchet.nttesttask

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import java.util.*

class Server {
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val processScope = CoroutineScope(Dispatchers.Default)

    private val instrumentNames = listOf(
        "EUR/USD_TOD",
        "GBP/USD_SPOT",
        "USD/JPY_TOM",
        "USD/CHF_SPOT",
        "USD/GBP_SPOT",
        "USD/CAD_TOM",
        "USD/RUB_TOM",
        "EUR/RUB_TOD",
        "CHF/RUB_TOM",
        "USD/AMD_SPOT",
        "EUR/GEL_SPOT",
        "UAH/RUB_SPOT",
        "USD/RUB_ON",
        "EUR/USD_TN",
    )

    fun subscribeToDeals(callback: (List<Deal>) -> Unit) {
        val currentTimeStamp = Date()

        processScope.launch {
            var deals = mutableListOf<Deal>()
            val dealsCount = (1_000_000L..1_001_000L).random()
            val dealsCountInPacket = 1_000
            var j = 0

            for (i in 0..dealsCount) {
                val deal = Deal(
                    id = i,
                    timeStamp = Date(currentTimeStamp.time + i),
                    instrumentName = instrumentNames.shuffled().first(),
                    price = getRandom(min = 60, max = 70),
                    amount = getRandom(min = 1_000_000, max = 50_000_000),
                    side = Deal.Side.values().toList().shuffled().first(),
                )
                deals.add(deal)

                j += 1

                if (j == dealsCountInPacket || i == dealsCount) {
                    j = 0
                    val delayValue = (0L..100L).random()
                    delay(delayValue)
                    val newDeals = deals

                    mainThreadHandler.post {
                        callback(newDeals)
                    }

                    deals = mutableListOf()
                }
            }
        }
    }

    private fun getRandom(min: Int, max: Int): Double {
        return min + Random.nextDouble() * (max - min)
    }

    data class Deal(
        val id: Long,
        val timeStamp: Date,
        val instrumentName: String,
        val price: Double,
        val amount: Double,
        val side: Side,
    ) {
        enum class Side {
            SELL, BUY
        }
    }
}
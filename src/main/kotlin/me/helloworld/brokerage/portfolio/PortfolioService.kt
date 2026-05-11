package me.helloworld.brokerage.portfolio

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode


@Service
class PortfolioService(
    private val accountClient: AccountClient,        // 계좌 API
    private val orderClient: OrderClient,            // 주문 API
    private val marketPriceClient: MarketPriceClient // 시세 API
) {

    suspend fun getAccountSummary(userId: String, accountId: Long): AccountSummaryResponse {
        return coroutineScope {
            val cashDeferred = async { accountClient.getCashBalance(userId, accountId) }
            val stocksDeferred = async { orderClient.getStockBalances(userId, accountId) }

            val cash = cashDeferred.await()
            val stocks = stocksDeferred.await()

            // 보유한 주식들의 티커코드만 추출
            val tickerCodes = stocks.map { it.tickerCode }.distinct()

            val prices = if (tickerCodes.isNotEmpty()) {
                marketPriceClient.getCurrentPrices(tickerCodes)
            } else {
                emptyMap()
            }

            calculateSummary(cash, stocks, prices)
        }
    }

    private fun calculateSummary(
        cash: BigDecimal,
        stocks: List<StockBalanceResponse>,
        prices: Map<String, BigDecimal>
    ): AccountSummaryResponse {

        // 현재가 합산액 계산
        val totalStockEvaluationAmount = stocks.sumOf { stock ->
            val currentPrice = prices[stock.tickerCode] ?: BigDecimal.ZERO
            currentPrice.multiply(BigDecimal.valueOf(stock.quantity))
        }

        // 총 매입액 계산
        val totalPurchaseAmount = stocks.sumOf { it.avgPrice.multiply(BigDecimal.valueOf(it.quantity)) }

        val totalAsset = totalStockEvaluationAmount.add(cash)
        val totalProfitAmount = totalStockEvaluationAmount.subtract(totalPurchaseAmount)

        val profitRate = if (totalPurchaseAmount > BigDecimal.ZERO) {
            totalProfitAmount.divide(totalPurchaseAmount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
        } else {
            BigDecimal.ZERO
        }

        return AccountSummaryResponse(
            totalAsset = totalAsset,
            cashBalance = cash,
            totalStockAmount = totalStockEvaluationAmount,
            totalPurchaseAmount = totalPurchaseAmount,
            totalProfitAmount = totalProfitAmount,
            profitRate = profitRate
        )
    }
}

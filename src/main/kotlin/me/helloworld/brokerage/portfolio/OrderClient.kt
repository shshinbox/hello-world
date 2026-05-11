package me.helloworld.brokerage.portfolio

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.math.BigDecimal

interface OrderClient {
    suspend fun getStockBalances(userId: String, accountId: Long): List<StockBalanceResponse>
}

@Component
@Profile("!local")
class OrderClientImpl(
    @param:Qualifier("orderWebClient") private val webClient: WebClient
) : OrderClient {

    override suspend fun getStockBalances(userId: String, accountId: Long): List<StockBalanceResponse> {
        return try {
            webClient.get()
                .uri { builder ->
                    builder.path("/api/v1/orders/balances")
                        .queryParam("accountId", accountId)
                        .build()
                }
                .retrieve()
                .awaitBody<List<StockBalanceResponse>>()
        } catch (e: Exception) {
            println("Order API 에러: ${e.message}")
            emptyList()
        }
    }
}

@Component
@Profile("local")
class FakeOrderClient : OrderClient {
    override suspend fun getStockBalances(userId: String, accountId: Long): List<StockBalanceResponse> {
        return listOf(
            StockBalanceResponse("005930", quantity=1, avgPrice= BigDecimal(200_000)),
            StockBalanceResponse("000660", quantity=1, avgPrice= BigDecimal(1_000_000)),
        )
    }
}
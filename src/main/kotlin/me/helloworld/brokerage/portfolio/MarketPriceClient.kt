package me.helloworld.brokerage.portfolio

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.math.BigDecimal


interface MarketPriceClient {
    suspend fun getCurrentPrices(tickerCodes: List<String>): Map<String, BigDecimal>
}

@Component
@Profile("!local")
class MarketPriceClientImpl(
    @param:Qualifier("marketWebClient") private val webClient: WebClient
) : MarketPriceClient {

    override suspend fun getCurrentPrices(tickerCodes: List<String>): Map<String, BigDecimal> {
        return try {
            webClient.get()
                .uri { builder ->
                    builder.path("/api/v1/market/prices")
                        .queryParam("codes", tickerCodes.joinToString(","))
                        .build()
                }
                .retrieve()
                .awaitBody<Map<String, BigDecimal>>()
        } catch (e: Exception) {
            println("시세 API 호출 실패: ${e.message}")
            emptyMap()
        }
    }
}

@Component
@Profile("local")
class FakeMarketPriceClient : MarketPriceClient {
    override suspend fun getCurrentPrices(tickerCodes: List<String>): Map<String, BigDecimal> {
        return mapOf(
            "005930" to BigDecimal(280_000),
            "000660" to BigDecimal(1_800_000)
        )
    }
}
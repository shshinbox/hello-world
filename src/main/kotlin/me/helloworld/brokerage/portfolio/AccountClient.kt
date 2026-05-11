package me.helloworld.brokerage.portfolio

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import java.math.BigDecimal

interface AccountClient {
    suspend fun getCashBalance(userId: String, accountId: Long): BigDecimal
}

@Component
@Profile("!local")
class AccountClientImpl(
    @param:Qualifier("accountWebClient") private val webClient: WebClient
) : AccountClient {

    override suspend fun getCashBalance(userId: String, accountId: Long): BigDecimal {
        return try {
            webClient.get()
                .uri { builder ->
                    builder.path("/api/v1/accounts/{accountId}/balance")
                        .queryParam("userId", userId)
                        .build(accountId)
                }
                .retrieve()
                .onStatus({ it.isError }) { response ->
                    response.bodyToMono<String>().map { msg ->
                        RuntimeException("계좌 팀 API 호출 실패: $msg")
                    }
                }
                .awaitBody<AccountBalanceResponse>()
                .cashBalance
        } catch (e: Exception) {
            println("Account API 에러 발생: ${e.message}")
            BigDecimal.ZERO
        }
    }
}

@Component
@Profile("local")
class FakeAccountClient : AccountClient {
    override suspend fun getCashBalance(userId: String, accountId: Long): BigDecimal {
        return BigDecimal("1000000")
    }
}
package me.helloworld.brokerage.portfolio

import java.math.BigDecimal


data class AccountSummaryResponse(
    val totalAsset: BigDecimal,         // 총 자산
    val cashBalance: BigDecimal,        // 예수금
    val totalStockAmount: BigDecimal,   // 주식 평가 금액 합계
    val totalPurchaseAmount: BigDecimal,// 총 매입 금액
    val totalProfitAmount: BigDecimal,  // 총 손익 금액
    val profitRate: BigDecimal          // 수익률
)

// 계좌 API 응답 DTO
data class AccountBalanceResponse(
    val accountId: Long,
    val cashBalance: BigDecimal
)

// 주문 API 응답 DTO
data class StockBalanceResponse(
    val tickerCode: String,
    val quantity: Long,
    val avgPrice: BigDecimal
)
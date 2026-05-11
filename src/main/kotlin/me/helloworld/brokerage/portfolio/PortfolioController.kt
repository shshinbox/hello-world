package me.helloworld.brokerage.portfolio

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/portfolio")
class PortfolioController(private val portfolioService: PortfolioService) {

    @GetMapping("/{accountId}")
    suspend fun getSummary(
        userContext: UserContext,
        @PathVariable accountId: Long
    ): AccountSummaryResponse {
        return portfolioService.getAccountSummary(userContext.userId, accountId)
    }
}
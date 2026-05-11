package me.helloworld.brokerage.portfolio

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

@Component
@Order(-1)
class HeaderFilter(
    @param:Value("\${jwt.secret}") private val secretKeyString: String
) : WebFilter {

    companion object {
        private val logger = LoggerFactory.getLogger(HeaderFilter::class.java)
    }

    private val signingKey: SecretKey = Keys.hmacShaKeyFor(
        secretKeyString.toByteArray(StandardCharsets.UTF_8)
    )

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst("Authorization")
        val userId = extractUserId(authHeader)

        // 리액티브 환경에서는 속성(Attributes)에 담아 공유하는 것이 정석
        exchange.attributes["X-USER-ID"] = userId

        return chain.filter(exchange)
    }

    private fun extractUserId(authHeader: String?): String {
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            return "GUEST"
        }

        return try {
            val token = authHeader.substring(7)
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .body

            claims.subject ?: "GUEST"
        } catch (e: Exception) {
            logger.error("JWT 파싱 중 오류 발생: {}", e.message, e)
            "GUEST"
        }
    }
}
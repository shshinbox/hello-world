package me.helloworld.brokerage.portfolio

import io.netty.channel.ChannelOption
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class WebClientConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder {
        return WebClient.builder()
    }

    @Bean
    fun accountWebClient(builder: WebClient.Builder, @Value("\${services.account.url}") url: String): WebClient {
        return builder.clone()
            .baseUrl(url)
            .clientConnector(createConnector(3000))
            .build()
    }

    @Bean
    fun orderWebClient(builder: WebClient.Builder, @Value("\${services.order.url}") url: String): WebClient {
        return builder.clone()
            .baseUrl(url)
            .clientConnector(createConnector(5000))
            .build()
    }

    @Bean
    fun marketWebClient(builder: WebClient.Builder, @Value("\${services.market.url}") url: String): WebClient {
        return builder.clone()
            .baseUrl(url)
            .clientConnector(createConnector(2000))
            .build()
    }

    private fun createConnector(timeoutMillis: Int): ReactorClientHttpConnector {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutMillis)
            .responseTimeout(Duration.ofMillis(timeoutMillis.toLong()))
        return ReactorClientHttpConnector(httpClient)
    }
}
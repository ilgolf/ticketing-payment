package me.golf.infra.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class RestClientConfig(
    @Value("\${toss.host}") private val host: String,
    @Value("\${toss.secret-key}") private val secretKey: String,
) {

    @Bean
    fun clientRequestFactory(): ClientHttpRequestFactory {
        val factory = SimpleClientHttpRequestFactory()

        factory.setConnectTimeout(Duration.ofSeconds(3))
        factory.setReadTimeout(Duration.ofSeconds(5))

        return factory
    }

    @Bean
    fun tossPaymentRestClient(): RestClient {
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))

        return RestClient.builder()
            .baseUrl(host)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "$BASIC_PREFIX$secretKey")
            .requestFactory(clientRequestFactory())
            .build()
    }

    companion object {
        const val BASIC_PREFIX = "Basic "
    }
}
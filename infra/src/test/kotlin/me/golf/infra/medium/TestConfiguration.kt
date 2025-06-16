package me.golf.infra.medium

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@TestConfiguration
class TestConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
}
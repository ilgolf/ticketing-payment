package me.golf.infra.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "spring.mail")
data class MailProperties @ConstructorBinding constructor(
    val host: String,
    val port: Int,
    val username: String,
    val password: String
)

package me.golf.infra.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["me.golf.infra.dao"])
@EntityScan(basePackages = ["me.golf.infra.entity"])
class InfraConfig
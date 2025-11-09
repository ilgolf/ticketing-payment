package me.golf.scheduler.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["me.golf.infra"])
class SchedulerInfraConfig

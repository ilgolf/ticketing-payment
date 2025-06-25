package me.golf.infra.medium;

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import java.time.Duration

abstract class TestContainer {

    companion object {
        private const val REDIS_IMAGE = "redis:latest"

        @JvmStatic
        @Container
        protected val REDIS_CONTAINER: GenericContainer<Nothing> = GenericContainer<Nothing>(REDIS_IMAGE)
            .apply { withExposedPorts(6379) }
            .apply { withReuse(true) }
            .apply { start() }

        @Container
        @JvmStatic
        val KAFKA_CONTAINER: KafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
            .withEmbeddedZookeeper()
            .withStartupTimeout(Duration.ofMinutes(2))
            .apply { start() }

        @JvmStatic
        @Container
        protected val MYSQL_CONTAINER = MySQLContainer<Nothing>("mysql:8.0")
            .apply { withDatabaseName("payment") }
            .apply { withUsername("test") }
            .apply { withPassword("1234") }
            .apply { withInitScript("init.sql") }
            .apply { withExposedPorts(3306) }
            .apply { withReuse(true) }
            .apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
            registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
            registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
            registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost)
            registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort)
            registry.add("spring.kafka.producer.bootstrap-servers") { KAFKA_CONTAINER.bootstrapServers }
            registry.add("spring.kafka.consumer.bootstrap-servers") { KAFKA_CONTAINER.bootstrapServers }
        }
    }
}
package me.golf.infra.config

object KafkaConstantPool {
    internal const val RETRIES_COUNT = 8
    internal const val RETRIES_BACK_OFF_MS = 3 * 1000
    internal const val DELIVERY_TIMEOUT_MS = 60 * 1000
    internal const val ACKS_VALUE = "1"
}

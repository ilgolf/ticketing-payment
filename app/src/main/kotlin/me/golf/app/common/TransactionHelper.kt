package me.golf.app.common

import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Component
class TransactionHelper(
    _transactionTemplate: TransactionTemplate,
) {
    init {
        transactionTemplate = _transactionTemplate
    }

    companion object {
        private lateinit var transactionTemplate: TransactionTemplate

        fun <R> execute(block: () -> R) {
            transactionTemplate.execute { block() }
        }
    }
}

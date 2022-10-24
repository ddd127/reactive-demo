package com.example.reactive.demo.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator

@Component
class TransactionalOperation @Autowired constructor(
    private val transactionalOperator: TransactionalOperator,
) {
    suspend fun <T> runTx(block: suspend CoroutineScope.() -> T) =
        transactionalOperator
            .transactional(mono(block = block))
            .awaitSingle()
}
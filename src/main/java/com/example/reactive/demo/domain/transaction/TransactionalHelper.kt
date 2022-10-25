package com.example.reactive.demo.domain.transaction

import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.reactive.awaitFirst
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TransactionalHelper @Autowired constructor(
    connectionFactory: ConnectionFactory,
) {
    val dslContext: DSLContext = DSL.using(connectionFactory).dsl()

    suspend fun <T : Any> defaultTxCoroutine(action: suspend TransactionContext.() -> T?): T {
        return dslContext.transactionPublisher { configuration ->
            val transactionContext = TransactionContext(configuration.dsl())
            mono(Dispatchers.Unconfined) { transactionContext.action() }
        }.awaitFirst()
    }

    fun <T : Any> defaultTxReactor(action: TransactionContext.() -> Publisher<T>): Publisher<T> {
        return dslContext.transactionPublisher { configuration ->
            val transactionContext = TransactionContext(configuration.dsl())
            transactionContext.action()
        }
    }
}

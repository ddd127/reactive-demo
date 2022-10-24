package com.example.reactive.demo.domain

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.r2dbc.R2dbcTransactionManagerAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.reactive.TransactionalOperator

@SpringBootConfiguration
@Import(
    R2dbcTransactionManagerAutoConfiguration::class,
)
class DomainSpringConfiguration {

    @Bean
    fun dslContext(connectionFactory: ConnectionFactory): DSLContext =
        DSL.using(connectionFactory).dsl()

    @Bean
    fun connectionFactoryTransactionManager(connectionFactory: ConnectionFactory): R2dbcTransactionManager =
        R2dbcTransactionManager(connectionFactory)

    @Bean
    fun transactionalOperator(reactiveTransactionManager: R2dbcTransactionManager): TransactionalOperator =
        TransactionalOperator.create(reactiveTransactionManager)
}
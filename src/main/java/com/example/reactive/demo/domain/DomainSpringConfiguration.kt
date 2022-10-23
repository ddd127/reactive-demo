package com.example.reactive.demo.domain

import io.r2dbc.spi.ConnectionFactory
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean

@SpringBootConfiguration
class DomainSpringConfiguration {

    @Bean
    fun dslContext(connectionFactory: ConnectionFactory): DSLContext =
        DSL.using(connectionFactory).dsl()
}

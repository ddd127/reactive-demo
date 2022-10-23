package com.example.reactive.demo

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureEmbeddedDatabase(
    provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
)
class ApplicationTests {

    @Test
    fun contextLoads() {
    }
}

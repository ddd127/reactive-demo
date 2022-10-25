package com.example.reactive.demo.web

import com.example.reactive.demo.domain.transaction.TransactionalHelper
import com.example.reactive.demo.domain.model.TestEntity
import com.example.reactive.demo.domain.model.TestEntityCreateRq
import com.example.reactive.demo.domain.repository.TestEntityRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux

@RestController
class TestEntityController @Autowired constructor(
    private val testEntityRepository: TestEntityRepository,
    private val transactionalHelper: TransactionalHelper,
) {

    @PostMapping("/create/coroutine")
    suspend fun createCoroutine(@RequestBody rqList: List<TestEntityCreateRq>): Flow<Long> = coroutineScope {
        transactionalHelper.defaultTxCoroutine {
            rqList.asFlow().map { testEntityRepository.createCoroutine(it) }
        }
    }

    @PostMapping("/create/reactor")
    fun createReactor(@RequestBody rqList: List<TestEntityCreateRq>): Flux<Long> {
        return transactionalHelper.defaultTxReactor {
            Flux.concat(
                rqList.map { testEntityRepository.createReactor(it) },
            )
        }.toFlux()
    }

    @GetMapping("/all")
    suspend fun getAll(): Flow<TestEntity> = coroutineScope {
        transactionalHelper.defaultTxCoroutine {
            testEntityRepository.getAll()
        }
    }

    @GetMapping("/single/{id}")
    suspend fun get(@PathVariable("id") id: Long): TestEntity? = coroutineScope {
        transactionalHelper.defaultTxCoroutine {
            testEntityRepository.get(id)
        }
    }
}

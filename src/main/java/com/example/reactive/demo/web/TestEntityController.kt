package com.example.reactive.demo.web

import com.example.reactive.demo.domain.model.TestEntity
import com.example.reactive.demo.domain.model.TestEntityCreateRq
import com.example.reactive.demo.domain.repository.TestEntityRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/testEntity")
class TestEntityController @Autowired constructor(
    private val testEntityRepository: TestEntityRepository,
) {

    @PostMapping("/create")
    suspend fun create(@RequestBody rq: TestEntityCreateRq): Long = coroutineScope {
        testEntityRepository.create(rq)
    }

    @GetMapping("/all")
    suspend fun getAll(): Flow<TestEntity> = coroutineScope {
        testEntityRepository.getAll()
    }

    @GetMapping("/{id}")
    suspend fun get(@PathVariable("id") id: Long): TestEntity? = coroutineScope {
        testEntityRepository.get(id)
    }
}

package com.example.reactive.demo.domain.repository

import com.example.reactive.demo.domain.transaction.TransactionContext
import com.example.reactive.demo.domain.db.Tables.TEST_ENTITY
import com.example.reactive.demo.domain.db.tables.records.TestEntityRecord
import com.example.reactive.demo.domain.model.TestEntity
import com.example.reactive.demo.domain.model.TestEntityCreateRq
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Repository
class TestEntityRepository {

    context(TransactionContext)
    suspend fun createCoroutine(entity: TestEntityCreateRq): Long =
        dslContext.insertInto(TEST_ENTITY)
            .set(modelToRecord(entity))
            .returning(TEST_ENTITY.ID)
            .awaitSingle().id.also {
                println("key = ${entity.key}")
            }

    context(TransactionContext)
    fun createReactor(entity: TestEntityCreateRq): Mono<Long> =
        dslContext.insertInto(TEST_ENTITY)
            .set(modelToRecord(entity))
            .returning(TEST_ENTITY.ID)
            .toMono().map { it.id }

    context(TransactionContext)
    suspend fun getAll(): Flow<TestEntity> =
        dslContext.selectFrom(TEST_ENTITY)
            .asFlow()
            .map(::recordToModel)

    context(TransactionContext)
    suspend fun get(id: Long): TestEntity? =
        dslContext.selectFrom(TEST_ENTITY)
            .where(TEST_ENTITY.ID.eq(id))
            .awaitFirstOrNull()
            ?.let(::recordToModel)

    companion object {

        fun modelToRecord(createRq: TestEntityCreateRq) =
            TestEntityRecord().apply {
                key = createRq.key
                value = createRq.value
            }

        fun recordToModel(record: TestEntityRecord) =
            TestEntity(
                record.id,
                record.key,
                record.value,
            )
    }
}

package com.example.reactive.demo.domain.repository

import com.example.reactive.demo.domain.db.Tables.TEST_ENTITY
import com.example.reactive.demo.domain.db.tables.records.TestEntityRecord
import com.example.reactive.demo.domain.model.TestEntity
import com.example.reactive.demo.domain.model.TestEntityCreateRq
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class TestEntityRepository @Autowired constructor(
    private val dslContext: DSLContext,
) {

    suspend fun create(entity: TestEntityCreateRq): Long =
        dslContext.insertInto(TEST_ENTITY)
            .set(modelToRecord(entity))
            .returning(TEST_ENTITY.ID)
            .awaitSingle().id

    fun getAll(): Flow<TestEntity> =
        dslContext.selectFrom(TEST_ENTITY)
            .asFlow()
            .map(::recordToModel)

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

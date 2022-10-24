package com.example.reactive.demo.domain.repository

import com.example.reactive.demo.domain.db.Tables.PERSON
import com.example.reactive.demo.domain.db.tables.records.PersonRecord
import com.example.reactive.demo.domain.model.Person
import com.example.reactive.demo.domain.model.PersonCreateRq
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class PersonRepository @Autowired constructor(
    private val dslContext: DSLContext,
) {

    suspend fun create(rq: PersonCreateRq): Long =
        dslContext.insertInto(PERSON)
            .set(rqToRecord(rq))
            .returning(PERSON.ID)
            .awaitSingle().id

    suspend fun update(person: Person) {
        dslContext.update(PERSON)
            .set(modelToRecord(person))
            .where(PERSON.ID.eq(person.id))
            .awaitSingle()
    }

    fun getAll(): Flow<Person> =
        dslContext.selectFrom(PERSON)
            .asFlow()
            .map(::recordToModel)

    suspend fun getLocking(ids: List<Long>): Flow<Person> =
        dslContext.selectFrom(PERSON)
            .where(PERSON.ID.`in`(ids))
            .orderBy(PERSON.ID)
            .forUpdate()
            .asFlow()
            .map(::recordToModel)

    suspend fun get(id: Long): Person? =
        dslContext.selectFrom(PERSON)
            .where(PERSON.ID.eq(id))
            .awaitFirstOrNull()
            ?.let(::recordToModel)

    companion object {
        fun rqToRecord(createRq: PersonCreateRq) = PersonRecord().apply {
            name = createRq.name
            balance = 0L
        }

        fun modelToRecord(model: Person) = PersonRecord().apply {
            id = model.id
            name = model.name
            balance = model.balance
        }

        fun recordToModel(record: PersonRecord) = with(record) {
            Person(
                id = id,
                name = name,
                balance = balance,
            )
        }
    }
}

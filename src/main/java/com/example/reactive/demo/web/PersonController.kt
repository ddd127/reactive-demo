package com.example.reactive.demo.web

import com.example.reactive.demo.domain.TransactionalOperation
import com.example.reactive.demo.domain.model.Person
import com.example.reactive.demo.domain.model.PersonCreateRq
import com.example.reactive.demo.domain.model.PersonPopUpRq
import com.example.reactive.demo.domain.model.PersonTransferRq
import com.example.reactive.demo.domain.repository.PersonRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/person")
class PersonController @Autowired constructor(
    private val personRepository: PersonRepository,
    private val tx: TransactionalOperation,
) {
    @PostMapping("/transfer")
    suspend fun transfer(@RequestBody rq: PersonTransferRq): ResponseEntity<String> = coroutineScope {
        tx.runTx {
            val (fromId, toId) = rq.fromPersonId to rq.toPersonId
            val persons = personRepository.getLocking(listOf(fromId, toId))
            val fromPerson = persons.first { it.id == fromId }
            val toPerson = persons.first { it.id == toId }
            personRepository.update(
                fromPerson.copy(
                    balance = fromPerson.balance - rq.amount
                )
            )
            personRepository.update(
                toPerson.copy(
                    balance = toPerson.balance + rq.amount
                )
            )
        }
        println("transfer ok")
        ResponseEntity.ok("ok transfer")
    }

    @PostMapping("/popup")
    suspend fun popup(@RequestBody rq: PersonPopUpRq): ResponseEntity<String> = coroutineScope {
        tx.runTx {
            val person = personRepository.getLocking(listOf(rq.personId)).first()
            personRepository.update(
                person.copy(
                    balance = person.balance + rq.amount,
                )
            )
        }
        ResponseEntity.ok("ok popup")
    }

    @PostMapping("/create")
    suspend fun create(@RequestBody rq: PersonCreateRq): Long = coroutineScope {
        personRepository.create(rq)
    }

    @GetMapping("/all")
    suspend fun getAll(): Flow<Person> = coroutineScope {
        personRepository.getAll()
    }

    @GetMapping("/{id}")
    suspend fun get(@PathVariable("id") id: Long): Person? = coroutineScope {
        personRepository.get(id)
    }
}
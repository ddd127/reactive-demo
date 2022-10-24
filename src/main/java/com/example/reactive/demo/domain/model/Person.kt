package com.example.reactive.demo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: Long,
    val name: String,
    val balance: Long,
)
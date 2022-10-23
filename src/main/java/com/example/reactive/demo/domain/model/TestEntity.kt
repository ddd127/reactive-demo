package com.example.reactive.demo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TestEntity(
    val id: Long,
    val key: String,
    val value: String?,
)

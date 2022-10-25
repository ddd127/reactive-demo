package com.example.reactive.demo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TestEntityCreateRq(
    val key: String,
    val value: String? = null,
)

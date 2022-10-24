package com.example.reactive.demo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonCreateRq(
    val name: String,
)
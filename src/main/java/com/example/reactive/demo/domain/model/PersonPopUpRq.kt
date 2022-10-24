package com.example.reactive.demo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonPopUpRq(
    val personId: Long,
    val amount: Long,
)
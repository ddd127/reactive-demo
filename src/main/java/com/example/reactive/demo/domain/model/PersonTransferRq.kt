package com.example.reactive.demo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonTransferRq(
    val fromPersonId: Long,
    val toPersonId: Long,
    val amount: Long,
)

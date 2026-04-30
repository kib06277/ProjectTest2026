package edu.hcvs.call_launches.model

data class Failure(
    val altitude: Int,
    val reason: String,
    val time: Int
)
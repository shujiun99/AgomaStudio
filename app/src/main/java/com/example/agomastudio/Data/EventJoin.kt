package com.example.agomastudio.Data

data class EventJoin(
    val okuId: String,
    val eventId: String,
){
    constructor(): this(
        "",
        ""
    )
}

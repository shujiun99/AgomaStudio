package com.example.agomastudio.Data

data class Help(
    val id: String,
    val name: String,
    val status: String,
    val helpStatus: String,
    val okuId: String,
    val description: String,
    val providerId: String,
    val eventId: String
){
    constructor(): this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}

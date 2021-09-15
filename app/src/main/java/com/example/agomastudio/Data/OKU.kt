package com.example.agomastudio.Data

data class OKU(
    val id: String,
    val name: String,
    val password: String,
    val email: String,
    val age: String,
    val gender: String,
    val address: String,
    val photo: String
){
    constructor() : this(
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

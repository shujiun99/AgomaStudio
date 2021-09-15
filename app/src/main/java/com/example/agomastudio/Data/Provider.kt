package com.example.agomastudio.Data

data class Provider(
    val id: String,
    val name: String,
    val password: String,
    val website: String,
    val address: String,
    val contact: String,
    val objective:String,
    val photo:String,
    val email: String){
    constructor() : this(
        "",
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

package com.example.agomastudio.Data

import java.util.*

data class Event(
  val id: String,
  val name:String,
  val date:String,
  val time:String,
  val description:String,
  val category:String,
  val status:String,
  val photo:String,
  val longitude:String,
  val latitude:String,
  val address: String,
  val providerId: String){
  constructor() : this(
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "")
}

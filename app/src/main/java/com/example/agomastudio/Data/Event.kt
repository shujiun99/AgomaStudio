package com.example.agomastudio.Data

import java.util.*

data class Event(val name:String, val date:String,val time:String, val description:String, val category:String,val status:String,val photo:String){
  constructor() : this("", "","","", "","", "")
}

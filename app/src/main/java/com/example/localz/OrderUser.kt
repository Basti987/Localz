package com.example.localz

class OrderUser(
    val orderId: String,
    val orderTime: String,
    val orderStatus:String,
    val orderCost:String,
    val orderBy:String,
    val orderTo:String
){
    constructor():this("","","","","","")
}


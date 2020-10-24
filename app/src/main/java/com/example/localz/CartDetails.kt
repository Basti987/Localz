package com.example.localz

class CartDetails(
    val Id:String,
    val productId: String,
    val title: String,
    val priceEach: String,
    val totalPrice: String,
    val totalQuantity: String

) {
    constructor():this("","","","","","")
}
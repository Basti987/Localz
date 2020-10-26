package com.example.localz

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerCartView: RecyclerView
    private var cartItemAdapter: CartItemAdapter? = null
    private lateinit var databaseRef: DatabaseReference
    private lateinit var cartItemList: List<CartDetails>
    private lateinit var modelOrderUser: OrderUser
    var allTotalPrice: Double = 0.00
    private lateinit var txtTotalItems: TextView
    private lateinit var btnClearCart: Button
    private lateinit var txtTotalPrice: TextView
    private lateinit var txtPrice: TextView
    private lateinit var txtDiscountPrice: TextView
    private lateinit var txtDeliveryCharge: TextView
    private lateinit var txtTotalAmount: TextView
    private lateinit var totalPayment: TextView
    private lateinit var btnContinue: Button
    var counter: Int = 0
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        txtTotalItems = findViewById(R.id.txtTotalItems)
        btnClearCart = findViewById(R.id.btnClearCart)
        txtTotalPrice = findViewById(R.id.txtTotalPrice)
        txtPrice = findViewById(R.id.txtPrice)
        txtDiscountPrice = findViewById(R.id.txtDiscountPrice)
        txtDeliveryCharge = findViewById(R.id.txtDeliveryCharge)
        txtTotalAmount = findViewById(R.id.txtTotalAmount)
        totalPayment = findViewById(R.id.TotalPayment)
        btnContinue = findViewById(R.id.btnContinue)
        cartItemList = ArrayList<CartDetails>()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)
        recyclerCartView = findViewById(R.id.cart_recycler_view)
        recyclerCartView.layoutManager = LinearLayoutManager(this)
        databaseRef = FirebaseDatabase.getInstance().reference.child("Shops").child("UsersCart")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                (cartItemList as ArrayList<CartDetails>).clear()
                for (i in p0.children) {
                    val id: String = i.child("id").value.toString()
                    val productId = i.child("productId").value.toString()
                    val title = i.child("title").value.toString()
                    val priceEach = i.child("priceEach").value.toString()
                    val totalPrice = i.child("totalPrice").value.toString()
                    val totalQuantity = i.child("totalQuantity").value.toString()
                    allTotalPrice += totalPrice.toDouble()
                    counter += 1
                    val obj =
                        CartDetails(
                            id, productId, title,
                            priceEach, totalPrice, totalQuantity
                        )
                    (cartItemList as ArrayList<CartDetails>).add(obj)
                }
                cartItemAdapter = CartItemAdapter(
                    this@CartActivity,
                    cartItemList
                )
                recyclerCartView.adapter = cartItemAdapter
                txtDeliveryCharge.text = "Rs.10"//delivery charge
                txtPrice.text = "Rs.${allTotalPrice}"

                var amount: Double = 0.0
                when {
                    counter > 1 -> {
                        txtTotalPrice.text = "Price(${counter} items)"
                        amount = allTotalPrice + 10.0//allTotalPrice+delivery charge
                    }
                    counter == 1 -> {
                        txtTotalPrice.text = "Price(${counter} item)"
                        amount = allTotalPrice + 10.0//allTotalPrice+delivery charge
                    }
                    counter == 0 -> {
                        txtTotalPrice.text = "Price(${counter} item)"
                        txtDeliveryCharge.text = "Rs.0"
                        amount = allTotalPrice + 0.0//allTotalPrice+delivery charge
                    }
                }
                txtTotalAmount.text = "Rs.${amount}"
                totalPayment.text = "Rs.${amount}"
                txtTotalItems.text = "Total Item :- ${counter}"
            }
        })
        btnContinue.setOnClickListener {
            if (cartItemList.isEmpty()) {
                Toast.makeText(this, "No item in the cart", Toast.LENGTH_SHORT).show()
            }
            submitOrder()
            clearCart()
        }

    }

    private fun clearCart() {
        databaseRef.removeValue()
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
    }

    private fun submitOrder() {
        progressDialog.setMessage("Placing Order....")
        progressDialog.show()
        val timestamp = System.currentTimeMillis().toString()
        val cost = allTotalPrice.toString().trim().replace("Rs.", "")
        val orderId = timestamp
        val orderTime = timestamp
        val orderStatus = "In Progress"
        val orderCost = cost
        val orderBy = "Ekagra Agrawal"
        val orderTo = "-MJ1__8Qk95zkMFthUa4"
        modelOrderUser = OrderUser(orderId, orderTime, orderStatus, orderCost, orderBy, orderTo)

        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Shops").child(orderTo).child("Orders")
        ref.child(timestamp).setValue(modelOrderUser)
            .addOnSuccessListener {
                for (i in 0 until cartItemList.size) {
                    val pId = cartItemList[i].productId
                    val id = cartItemList[i].Id
                    val cost = cartItemList[i].totalPrice
                    val price = cartItemList[i].priceEach
                    val quantity = cartItemList[i].totalQuantity
                    val name = cartItemList[i].title
                    val headers = HashMap<String, String>()
                    headers["pId"] = pId
                    headers["name"] = name
                    headers["cost"] = cost
                    headers["price"] = price
                    headers["quantity"] = quantity
                    ref.child(timestamp).child("Items").child(pId).setValue(headers)
                }
                progressDialog.dismiss()
                Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, OrderUserDetailsActivity::class.java)
                intent.putExtra("orderTo", orderTo)
                intent.putExtra("orderId", orderId)
                startActivity(intent)
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }


    }


}

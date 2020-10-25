package com.example.localz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderUserDetailsActivity : AppCompatActivity() {
    private lateinit var orderIdTv: TextView
    private lateinit var orderDate: TextView
    private lateinit var orderStatusTv: TextView
    private lateinit var shopName: TextView
    private lateinit var items: TextView
    private lateinit var amount: TextView
    private lateinit var deliveryAddress: TextView
    private lateinit var recyclerOrderedItems: RecyclerView
    private lateinit var orderItemList: List<OrderedItem>
    private lateinit var orderedItemAdapter: AdapterOrderedItem
    private var orderId: String? = "100"
    private var orderTo: String? = "ABC"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_user_details)
        orderIdTv = findViewById(R.id.orderId)
        orderDate = findViewById(R.id.orderDate)
        orderStatusTv = findViewById(R.id.orderStatus)
        shopName = findViewById(R.id.shop)
        items = findViewById(R.id.items)
        amount = findViewById(R.id.orderCost)
        deliveryAddress = findViewById(R.id.orderDeliveryAddress)
        recyclerOrderedItems = findViewById(R.id.recyclerOrderedItems)
        orderItemList = ArrayList<OrderedItem>()


        orderId = intent.getStringExtra("orderId")
        orderTo = intent.getStringExtra("orderTo")

        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Shops")
        databaseRef.child(orderTo.toString()).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val shop = snapshot.child("name").value.toString()
                shopName.text = shop
            }

        })
        databaseRef.child(orderTo.toString()).child("Orders").child(orderId.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val orderBy: String = snapshot.child("orderBy").value.toString()
                    val orderCost: String = snapshot.child("orderCost").value.toString()
                    val orderId: String = snapshot.child("orderId").value.toString()
                    val orderStatus: String = snapshot.child("orderStatus").value.toString()
                    val orderTime: String = snapshot.child("orderTime").value.toString()
                    val orderTo: String = snapshot.child("orderTo").value.toString()

                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    val date=Date(orderTime.toLong())
                    val formattedDate = sdf.format(date)
                    when (orderStatus) {
                        "In Progress" -> {
                            orderStatusTv.setTextColor(resources.getColor(R.color.green))
                        }
                        "Completed" -> {
                            orderStatusTv.setTextColor(resources.getColor(R.color.acidGreen))
                        }
                        "Cancelled" -> {
                            orderStatusTv.setTextColor(resources.getColor(R.color.red))
                        }
                    }
                    orderIdTv.text = "OD${orderId}"
                    orderStatusTv.text = orderStatus
                    amount.text = "Rs.${orderCost}"
                    orderDate.text = formattedDate
                }

            })
        databaseRef.child(orderTo.toString()).child("Orders").child(orderId.toString())
            .child("Items").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val obj = OrderedItem(
                            i.child("pId").value.toString(),
                            i.child("name").value.toString(),
                            i.child("cost").value.toString(),
                            i.child("price").value.toString(),
                            i.child("quantity").value.toString()
                        )
                        (orderItemList as ArrayList<OrderedItem>).add(obj)

                    }
                    orderedItemAdapter =
                        AdapterOrderedItem(this@OrderUserDetailsActivity, orderItemList)
                    recyclerOrderedItems.adapter = orderedItemAdapter
                    items.text= snapshot.childrenCount.toString()
                }

            })
    }
}



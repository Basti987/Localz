package com.example.localz

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailsSellerActivity : AppCompatActivity() {
    private lateinit var orderIdTv: TextView
    private lateinit var dateTv: TextView
    private lateinit var orderStatusTv: TextView
    private lateinit var itemsTv: TextView
    private lateinit var amountTv: TextView
    private lateinit var deliveryAddressTv: TextView
    private lateinit var recyclerOrderedSellerItem: RecyclerView
    private lateinit var orderedItemList: List<OrderedItem>
    private lateinit var orderedItemAdapter: AdapterOrderedItem
    private lateinit var btnEdit: Button
    private var orderId: String? = "100"
    private var orderBy: String? = "ABC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details_seller)
        orderId = intent.getStringExtra("orderId")
        orderBy = intent.getStringExtra("orderBy")
        orderIdTv = findViewById(R.id.txtOrderId)
        dateTv = findViewById(R.id.txtOrderDate)
        orderStatusTv = findViewById(R.id.txtOrderStatus)
        itemsTv = findViewById(R.id.txtItems)
        amountTv = findViewById(R.id.txtOrderCost)
        deliveryAddressTv = findViewById(R.id.txtOrderDeliveryAddress)
        orderedItemList = ArrayList<OrderedItem>()
        btnEdit = findViewById(R.id.btnEdit)
        recyclerOrderedSellerItem = findViewById(R.id.recyclerOrderedSellerItem)
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Shops")
        databaseRef.child("-MJ1__8Qk95zkMFthUa4").child("Orders").child(orderId.toString())
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
                    val date = Date(orderTime.toLong())
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
                    amountTv.text = "Rs.${orderCost}"
                    dateTv.text = formattedDate

                }
            })
        databaseRef.child("-MJ1__8Qk95zkMFthUa4").child("Orders").child(orderId.toString())
            .child("Items").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    (orderedItemList as ArrayList<OrderedItem>).clear()
                    for (i in snapshot.children) {
                        val obj = OrderedItem(
                            i.child("pId").value.toString(),
                            i.child("name").value.toString(),
                            i.child("cost").value.toString(),
                            i.child("price").value.toString(),
                            i.child("quantity").value.toString()
                        )
                        (orderedItemList as ArrayList<OrderedItem>).add(obj)
                    }
                    orderedItemAdapter =
                        AdapterOrderedItem(this@OrderDetailsSellerActivity, orderedItemList)
                    recyclerOrderedSellerItem.adapter = orderedItemAdapter
                    itemsTv.text = snapshot.childrenCount.toString()
                }

            })
        btnEdit.setOnClickListener {
            editOrderStatusDialog()
            btnEdit.visibility= View.GONE
        }
    }

    private fun editOrderStatusDialog() {
        val options = arrayOf("In Progress", "Completed", "Cancelled")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Order Status")
        builder.setSingleChoiceItems(options, -1) { dialog, which ->
            val selectedItem = options[which]
            editOrderStatus(selectedItem)
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun editOrderStatus(selectedItem: String) {
        val headers = hashMapOf<String, Any>()
        headers["orderStatus"] = selectedItem
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Shops")
        ref.child("-MJ1__8Qk95zkMFthUa4").child("Orders").child(orderId.toString())
            .updateChildren(headers).addOnSuccessListener {
                Toast.makeText(this, "Order is $selectedItem", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, "Order is ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
}

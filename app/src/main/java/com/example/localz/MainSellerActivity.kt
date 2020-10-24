package com.example.localz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainSellerActivity : AppCompatActivity() {
    private lateinit var recyclerOrderShop: RecyclerView
    private var sAdapter: AdapterOrderShop? = null
    private lateinit var shopOrderList: List<ModelOrderShop>
    private lateinit var sDatabaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_seller)
        recyclerOrderShop = findViewById(R.id.recyclerShopOrders)
        recyclerOrderShop.layoutManager = LinearLayoutManager(this)
        shopOrderList = ArrayList<ModelOrderShop>()
        sDatabaseReference = FirebaseDatabase.getInstance().reference.child("Shops")
        sDatabaseReference.child("-MJ1__8Qk95zkMFthUa4").child("Orders")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    (shopOrderList as ArrayList).clear()
                    for (i in snapshot.children) {
                        val obj = ModelOrderShop(
                            i.child("orderId").value.toString(),
                            i.child("orderTime").value.toString(),
                            i.child("orderStatus").value.toString(),
                            i.child("orderCost").value.toString(),
                            i.child("orderBy").value.toString(),
                            i.child("orderTo").value.toString()
                        )
                        (shopOrderList as ArrayList).add(obj)
                    }
                    sAdapter = AdapterOrderShop(this@MainSellerActivity, shopOrderList)
                    recyclerOrderShop.adapter = sAdapter
                }

            })
    }
}

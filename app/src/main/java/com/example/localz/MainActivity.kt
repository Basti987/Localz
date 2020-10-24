package com.example.localz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    //lateinit var tabLayout: TabLayout
    //lateinit var viewPager:ViewPager
    //lateinit var toolbar: Toolbar
    private lateinit var recyclerOrderUser: RecyclerView
    private var oAdapter: AdapterOrderUser? = null
    private lateinit var orderList: List<OrderUser>
    private lateinit var mDatabase: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //tabLayout=findViewById(R.id.TabLayout)
        //viewPager=findViewById(R.id.viewPager)
        //toolbar=findViewById(R.id.myToolbar)

        //setSupportActionBar(toolbar)
        //supportActionBar?.title="Localz"
        // viewPager.adapter=ViewPagerAdapter(supportFragmentManager)
        //tabLayout.setupWithViewPager(viewPager)
        recyclerOrderUser = findViewById(R.id.orders)
        recyclerOrderUser.layoutManager = LinearLayoutManager(this)
        orderList = ArrayList<OrderUser>()
        mDatabase = FirebaseDatabase.getInstance().reference.child("Shops")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                (orderList as ArrayList<OrderUser>).clear()
                for (i in snapshot.children) {
                    val uid = i.ref.key
                    val databaseRef: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("Shops")
                            .child(uid.toString()).child("Orders")
                    databaseRef.orderByChild("orderBy").equalTo("Ekagra Agrawal")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {

                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (i in snapshot.children) {
                                        val obj = OrderUser(
                                            i.child("orderId").value.toString(),
                                            i.child("orderTime").value.toString(),
                                            i.child("orderStatus").value.toString(),
                                            i.child("orderCost").value.toString(),
                                            i.child("orderBy").value.toString(),
                                            i.child("orderTo").value.toString()
                                        )
                                        (orderList as ArrayList<OrderUser>).add(obj)
                                    }
                                    oAdapter = AdapterOrderUser(this@MainActivity, orderList)
                                    recyclerOrderUser.adapter = oAdapter
                                }
                            }
                        })
                }
            }

        })

    }

}

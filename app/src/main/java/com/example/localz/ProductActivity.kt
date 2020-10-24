package com.example.localz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ProductActivity : AppCompatActivity() {
    private lateinit var recyclerProductView: RecyclerView
    private var productAdapter: ProductAdapter? = null
    private lateinit var databaseRef: DatabaseReference
    lateinit var mCart: List<CartFields>
    private lateinit var btnGoToCart:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        mCart = ArrayList<CartFields>()
        btnGoToCart=findViewById(R.id.btnGoToCart)
        recyclerProductView = findViewById(R.id.recyclerProductView)
        recyclerProductView.layoutManager= LinearLayoutManager(this)
        databaseRef = FirebaseDatabase.getInstance().reference.child("Shops").child("Products")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProductActivity, error.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    val obj = CartFields(
                        i.child("productId").value.toString(),
                        i.child("productTitle").value.toString(),
                        i.child("productDescription").value.toString(),
                        i.child("productCategory").value.toString(),
                        i.child("productPrice").value.toString(),
                        i.child("productUrl").value.toString()
                    )
                    (mCart as ArrayList<CartFields>).add(obj)
                }
                productAdapter = ProductAdapter(this@ProductActivity, mCart)
                recyclerProductView.adapter = productAdapter
            }
        })
        btnGoToCart.setOnClickListener {
            val intent= Intent(this,CartActivity::class.java)
            startActivity(intent)
        }
    }
}

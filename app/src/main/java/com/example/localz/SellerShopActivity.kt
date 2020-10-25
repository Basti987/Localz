package com.example.localz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class SellerShopActivity : AppCompatActivity() {
    private lateinit var recyclerShopView:RecyclerView
    private lateinit var mDatabaseRef:DatabaseReference
    private  var mAdapter:ImageAdapter?=null
    lateinit var mUploads:List<Upload>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_shop)
        recyclerShopView=findViewById(R.id.recyclerShopView)
        recyclerShopView.layoutManager=LinearLayoutManager(this)
        mUploads=ArrayList<Upload>()
        mDatabaseRef=FirebaseDatabase.getInstance().reference.child("Shops")
        mDatabaseRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    val obj = Upload(
                        i.child("name").value.toString(),
                        i.child("address").value.toString(),
                        i.child("imageUrl").value.toString(),
                        i.child("city").value.toString()
                    )
                    //if(i.child("city").value.toString()==sellercity){}
                    (mUploads as ArrayList<Upload>).add(obj)
                }
                mAdapter=ImageAdapter(this@SellerShopActivity,mUploads)
                recyclerShopView.adapter=mAdapter
            }

        })
    }
}

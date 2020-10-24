package com.example.localz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageTask
import java.util.*
import kotlin.collections.ArrayList

class NewActivity : AppCompatActivity() {
    private lateinit var recyclerShopView: RecyclerView
    private lateinit var mDatabaseRef: DatabaseReference
    private var mAdapter: ImageAdapter? = null
    lateinit var mUploads: List<Upload>

    lateinit var user: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
        val imageUploadId: String? = intent.getStringExtra("ShopId")

        recyclerShopView = findViewById(R.id.recyclerShopView)
        recyclerShopView.layoutManager = LinearLayoutManager(this)
        mUploads = ArrayList<Upload>()
        mDatabaseRef =
            FirebaseDatabase.getInstance().reference.child("Shops")
        mDatabaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    val obj = Upload(
                        i.child("name").value.toString(),
                        i.child("address").value.toString(),
                        i.child("imageUrl").value.toString()
                    )
                    (mUploads as ArrayList<Upload>).add(obj)
                }

                mAdapter = ImageAdapter(this@NewActivity, mUploads,object:ImageAdapter.OnItemClickListener
                {
                    override fun OnAddItemClick(upload: Upload) {
                        val timestamp:String=""+System.currentTimeMillis()

                    }

                })
                recyclerShopView.adapter = mAdapter

            }

        })


    }
}


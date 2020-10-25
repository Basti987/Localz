package com.example.localz

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class CartItemAdapter(val context: Context, private val cartItems: List<CartDetails>) :
    RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_single_item, parent, false)
        return CartViewHolder(view)

    }

    override fun getItemCount(): Int {
        return cartItems.size
    }


    private lateinit var databaseRef: DatabaseReference
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val mcartItems = cartItems[position]
        holder.itemTitle.text = mcartItems.title
        holder.itemPriceEach.text = "Rs. ${mcartItems.priceEach}/each"
        holder.itemTotalQuantity.text = "Qty:- ${mcartItems.totalQuantity}"
        holder.itemTotalPrice.text = "Total Price:- ${mcartItems.totalPrice}"
        holder.btnRemoveItem.setOnClickListener {
            databaseRef = FirebaseDatabase.getInstance().reference.child("Shops").child("UsersCart")
            databaseRef.child(mcartItems.productId).removeValue()
            val intent= Intent(context,ProductActivity::class.java)
            context.startActivity(intent)
        }

    }
    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTitle: TextView = view.findViewById(R.id.txtTitleName)
        val itemTotalPrice: TextView = view.findViewById(R.id.txtItemTotalPrice)
        val itemTotalQuantity: TextView = view.findViewById(R.id.txtItemTotalQuantity)
        val itemPriceEach: TextView = view.findViewById(R.id.txtItemPriceEach)
        val btnRemoveItem: Button = view.findViewById(R.id.btnRemove)


    }
}
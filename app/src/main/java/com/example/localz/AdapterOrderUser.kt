package com.example.localz

import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AdapterOrderUser(val context: Context, private val orderList: List<OrderUser>) :
    RecyclerView.Adapter<AdapterOrderUser.HolderOrderUser>() {
    class HolderOrderUser(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.txtOrderId)
        val orderDate: TextView = view.findViewById(R.id.textOrderDate)
        val shopName: TextView = view.findViewById(R.id.shopName)
        val orderStatus: TextView = view.findViewById(R.id.orderStatus)
        val orderPayment: TextView = view.findViewById(R.id.totalAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderOrderUser {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_single_row, parent, false)
        return HolderOrderUser(view)

    }

    override fun getItemCount(): Int {
        return orderList.size

    }

    override fun onBindViewHolder(holder: HolderOrderUser, position: Int) {
        val orderUser = orderList[position]
        holder.orderId.text = "OrderId:- OD${orderUser.orderId}"
        holder.orderPayment.text = "Amount:- Rs. ${orderUser.orderCost}"
        holder.orderStatus.text = orderUser.orderStatus
        loadShopInfo(orderUser, holder)
        if (orderUser.orderStatus == "In Progress") {
            holder.orderStatus.setTextColor(context.resources.getColor(R.color.green))
        }
        if (orderUser.orderStatus == "Confirmed") {
            holder.orderStatus.setTextColor(context.resources.getColor(R.color.acidGreen))
        }
        if (orderUser.orderStatus == "Cancelled") {
            holder.orderStatus.setTextColor(context.resources.getColor(R.color.red))
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date=Date(orderUser.orderTime.toLong())
        val formattedDate = sdf.format(date)
        holder.orderDate.text = formattedDate
        holder.itemView.setOnClickListener {
            val intent=Intent(context,OrderUserDetailsActivity::class.java)
            intent.putExtra("orderTo",orderUser.orderTo)
            intent.putExtra("orderId",orderUser.orderId)
            context.startActivity(intent)
        }

    }

    private fun loadShopInfo(orderUser: OrderUser, holder: HolderOrderUser) {
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Shops")
        ref.child(orderUser.orderTo).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {


            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val shop = snapshot.child("name").value.toString()
                holder.shopName.text = shop
            }

        })

    }
}
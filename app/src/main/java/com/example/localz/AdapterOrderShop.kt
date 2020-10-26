package com.example.localz

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class AdapterOrderShop(val context: Context, private val orderSeller: List<ModelOrderShop>) :
    RecyclerView.Adapter<AdapterOrderShop.HolderOrderShop>() {
    class HolderOrderShop(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.orderIdTv)
        val orderDate: TextView = view.findViewById(R.id.orderDateTv)
        val orderAmount: TextView = view.findViewById(R.id.orderAmountTv)
        val orderStatus: TextView = view.findViewById(R.id.orderStatusTv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderOrderShop {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_order_seller, parent, false)
        return HolderOrderShop(view)
    }

    override fun getItemCount(): Int {
        return orderSeller.size
    }

    override fun onBindViewHolder(holder: HolderOrderShop, position: Int) {
        val orderShop = orderSeller[position]
        holder.orderId.text = "OD${orderShop.orderId}"
        holder.orderAmount.text = "Amount:-Rs.${orderShop.orderCost}"
        holder.orderStatus.text = orderShop.orderStatus
        if (orderShop.orderStatus == "In Progress") {
            holder.orderStatus.setTextColor(context.resources.getColor(R.color.green))
        }
        if (orderShop.orderStatus == "Confirmed") {
            holder.orderStatus.setTextColor(context.resources.getColor(R.color.acidGreen))
        }
        if (orderShop.orderStatus == "Cancelled") {
            holder.orderStatus.setTextColor(context.resources.getColor(R.color.red))
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = Date(orderShop.orderTime.toLong())
        val formattedDate = sdf.format(date)
        holder.orderDate.text = formattedDate

        holder.itemView.setOnClickListener {
            val intent=Intent(context,OrderDetailsSellerActivity::class.java)
            intent.putExtra("orderId",orderShop.orderId)
            intent.putExtra("orderBy",orderShop.orderBy)//uid of the customer
            context.startActivity(intent)
        }
    }


}
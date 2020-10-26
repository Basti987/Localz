package com.example.localz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//done cloning in android studio
class AdapterOrderedItem(val context: Context,private val orderedItemList:List<OrderedItem>):RecyclerView.Adapter<AdapterOrderedItem.HolderOrderedItem>() {
    class HolderOrderedItem(view: View) :RecyclerView.ViewHolder(view){
        val itemTitle:TextView=view.findViewById(R.id.itemTitle)
        val itemPrice:TextView=view.findViewById(R.id.itemPrice)
        val totalQuantity:TextView=view.findViewById(R.id.totalQuantity)
        val totalPrice:TextView=view.findViewById(R.id.totalPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderOrderedItem {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.row_order_user_item,parent,false)
        return HolderOrderedItem(view)
    }

    override fun getItemCount(): Int {
       return orderedItemList.size
    }

    override fun onBindViewHolder(holder: HolderOrderedItem, position: Int) {
        val orderedItem=orderedItemList[position]
        holder.itemTitle.text=orderedItem.name
        holder.itemPrice.text="Rs. ${orderedItem.price}"
        holder.totalPrice.text="Rs. ${orderedItem.cost}"
        holder.totalQuantity.text=orderedItem.quantity


    }
}
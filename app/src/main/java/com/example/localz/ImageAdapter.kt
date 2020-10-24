package com.example.localz

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(
    val context: Context,
    private val mupload: List<Upload>,
    private val listener:OnItemClickListener
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.images_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mupload.size

    }

    interface OnItemClickListener {
        fun OnAddItemClick(upload: Upload)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val upload = mupload[position]
        Glide.with(context).load(upload.imageUrl).into(holder.imgShops)
        holder.txtName.text = upload.name
        holder.txtAddress.text = upload.address
        holder.btnAddToCart.setOnClickListener {
            holder.btnAddToCart.visibility = View.GONE
            holder.btnRemoveFromCart.visibility = View.VISIBLE
            listener.OnAddItemClick(upload)
        }
        holder.cardView.setOnClickListener {

        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ImageViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
        val imgShops: ImageView = view.findViewById(R.id.imgShops)
        val txtName: TextView = view.findViewById(R.id.txtShopName)
        val txtAddress: TextView = view.findViewById(R.id.txtShopAddress)
        val btnAddToCart: Button = view.findViewById(R.id.addToCart)
        val btnRemoveFromCart: Button = view.findViewById(R.id.removeFromCart)
        val cardView:CardView=view.findViewById(R.id.shop_card_view)

    }

}
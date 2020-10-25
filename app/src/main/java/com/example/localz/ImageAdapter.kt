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
    private val mupload: List<Upload>
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.images_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mupload.size

    }



    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val upload = mupload[position]
        Glide.with(context).load(upload.imageUrl).into(holder.imgShops)
        holder.txtName.text = upload.name
        holder.txtAddress.text = upload.address
        holder.txtCity.text=upload.city
        holder.itemView.setOnClickListener {
            //val intent=Intent(context,ProductActivity)
            //intent.putExtra("uid",shopUid)
            //context.startActivity(intent)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ImageViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
        val imgShops: ImageView = view.findViewById(R.id.imgShops)
        val txtName: TextView = view.findViewById(R.id.txtShopName)
        val txtAddress: TextView = view.findViewById(R.id.txtShopAddress)
        val txtCity:TextView=view.findViewById(R.id.txtCity)

    }

}
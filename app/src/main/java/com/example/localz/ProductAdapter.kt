package com.example.localz

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ProductAdapter(val context: Context, private val product: List<CartFields>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_single_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return product.size

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = product[position]
        Glide.with(context).load(product.productUrl).into(holder.imgProduct)
        holder.titleProduct.text = product.productTitle
        holder.categoryProduct.text = product.productCategory
        holder.descriptionProduct.text = product.productDescription
        holder.priceProduct.text = product.productPrice
        holder.btnAdd.setOnClickListener {
            showQuantityDialog(product)
        }

    }

    private var cost: Double = 0.0
    private var finalCost: Double = 0.0
    private var quantity: Int = 0

    private fun showQuantityDialog(product: CartFields) {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_list, null)
        val productName: TextView = view.findViewById(R.id.ProductName)
        val productDesc: TextView = view.findViewById(R.id.Description)
        val originalPrice: TextView = view.findViewById(R.id.ProductPrice)
        val finalPrice: TextView = view.findViewById(R.id.ProductFinalPrice)
        val productImage: ImageView = view.findViewById(R.id.ProductImage)
        val btnIncrease: Button = view.findViewById(R.id.btnIncrease)
        val btnDecrease: Button = view.findViewById(R.id.btnDecrease)
        val finalQuantity: TextView = view.findViewById(R.id.txtCounter)
        val btnAddToCart: Button = view.findViewById(R.id.btncontinue)

        val productId = product.productId
        val productTitle = product.productTitle
        val productDescription = product.productDescription
        val productUrl = product.productUrl
        //var price:String
        quantity = 1
        cost = product.productPrice.toDouble()
        finalCost = product.productPrice.toDouble()
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        Picasso.get().load(productUrl).into(productImage)
        productName.text = productTitle
        productDesc.text = productDescription
        originalPrice.text = product.productPrice
        finalPrice.text = finalCost.toString()
        finalQuantity.text = quantity.toString()
        val dialog = builder.create()
        dialog.show()
        btnIncrease.setOnClickListener {
            finalCost += cost
            quantity++
            val amount = finalCost
            finalPrice.text = amount.toString()
            finalQuantity.text = quantity.toString()
        }
        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                finalCost -= cost
                quantity--
                val amount = finalCost
                finalPrice.text = amount.toString()
                finalQuantity.text = quantity.toString()
            }
        }
        btnAddToCart.setOnClickListener {
            val title = productName.text.toString().trim()
            val priceEach = originalPrice.text.toString().trim()
            val finalPr = finalPrice.text.toString().trim()
            val finalQ = finalQuantity.text.toString().trim()

            addToCart(productId, title, priceEach, finalPr, finalQ)
            dialog.dismiss()

        }

    }

    private var itemId: Int = 1
    private lateinit var cartDatabase: DatabaseReference
    private lateinit var cart: CartDetails
    private fun addToCart(
        productId: String,
        title: String,
        priceEach: String,
        finalPr: String,
        finalQ: String
    ) {
        itemId++
        cartDatabase = FirebaseDatabase.getInstance().reference.child("Shops")
        cart = CartDetails(
            itemId.toString(), productId, title, priceEach, finalPr, finalQ
        )
        cartDatabase.child("UsersCart").child(productId).setValue(cart)
        Toast.makeText(context, "Added To Cart", Toast.LENGTH_SHORT).show()
    }

    inner class ProductViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
        val imgProduct: ImageView = view.findViewById(R.id.Product)
        val titleProduct: TextView = view.findViewById(R.id.txtTitle)
        val descriptionProduct: TextView = view.findViewById(R.id.txtDescription)
        val categoryProduct: TextView = view.findViewById(R.id.txtCategory)
        val priceProduct: TextView = view.findViewById(R.id.price)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
    }

}
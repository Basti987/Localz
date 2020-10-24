package com.example.localz

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RatingActivity : AppCompatActivity() {
    private lateinit var btnAddProduct: Button
    private lateinit var etPrice: EditText
    private lateinit var etCategory: EditText
    private lateinit var etDescription: EditText
    private lateinit var etTitle: EditText
    private lateinit var imgProducts: ImageView
    private lateinit var imagePath: Uri
    private lateinit var cart: CartFields
    private lateinit var mCartDatabaseRef: DatabaseReference
    private lateinit var btnGoToMyShop: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        btnAddProduct = findViewById(R.id.btnAddProduct)
        etPrice = findViewById(R.id.etPrice)
        etCategory = findViewById(R.id.etCategory)
        etDescription = findViewById(R.id.etDescription)
        etTitle = findViewById(R.id.etTitle)
        imgProducts = findViewById(R.id.imgProducts)
        btnGoToMyShop = findViewById(R.id.btnGoToMyShop)

        mCartDatabaseRef = FirebaseDatabase.getInstance().reference.child("Shops")
        imgProducts.setOnClickListener {
            startImageChooser()

        }
        btnAddProduct.setOnClickListener {
            uploadData()

        }
        btnGoToMyShop.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun uploadData() {
        if (imagePath != null) {
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading.....")
            pd.show()
            val productRef =
                FirebaseStorage.getInstance().reference.child("uploads/" + System.currentTimeMillis() + ".jpg")
            productRef.putFile(imagePath)
                .addOnSuccessListener { p0 ->
                    pd.dismiss()
                    productRef.downloadUrl.addOnSuccessListener {
                        val imageUrl: Uri = it
                        val timestamp: String = "" + System.currentTimeMillis()
                        cart = CartFields(
                            timestamp,
                            etTitle.text.toString().trim(),
                            etDescription.text.toString().trim(),
                            etCategory.text.toString().trim(),
                            etPrice.text.toString().trim(),
                            imageUrl.toString()
                        )
                        mCartDatabaseRef.child("Products").child(timestamp).setValue(cart)
                        Toast.makeText(this, "Product Added", Toast.LENGTH_SHORT).show()
                        clearData()

                    }
                }
                .addOnFailureListener { p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()

                }
                .addOnProgressListener { p0 ->
                    val progress = (100.0 * p0.bytesTransferred / p0.totalByteCount)
                    pd.setMessage("Uploading ${progress.toInt()}%")
                }

        }
    }

    private fun startImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Product Image"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            imagePath = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imagePath)
            imgProducts.setImageBitmap(bitmap)
        }
    }

    private fun clearData() {
        etTitle.setText("")
        etDescription.setText("")
        etCategory.setText("")
        etPrice.setText("")
        imgProducts.setImageResource(R.drawable.dummy_image)
    }
}

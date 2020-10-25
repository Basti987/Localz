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

class UploadImage : AppCompatActivity() {
    private lateinit var btnChoose: Button
    private lateinit var btnUpload: Button
    private lateinit var imgProduct: ImageView
    private lateinit var filepath: Uri
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var etName: EditText
    private lateinit var btnSave: Button
    private lateinit var etAddress: EditText
    private lateinit var etCity:EditText
    private lateinit var upload: Upload


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)
        btnChoose = findViewById(R.id.btnChooseFile)
        btnUpload = findViewById(R.id.btnUpload)
        imgProduct = findViewById(R.id.imgProduct)
        etName = findViewById(R.id.etName)
        etAddress = findViewById(R.id.etAddress)
        btnSave = findViewById(R.id.btnSave)
        etCity=findViewById(R.id.etCity)


        mDatabaseRef = FirebaseDatabase.getInstance().reference.child("Shops")
        btnChoose.setOnClickListener {
            startFileChooser()
        }
        btnUpload.setOnClickListener {
            uploadFile()
        }
        btnSave.setOnClickListener {
            startActivity(Intent(this, UserShopActivity::class.java))
        }

    }

    private fun uploadFile() {
        if (filepath != null) {
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            val imageRef =
                FirebaseStorage.getInstance().reference.child("uploads/" + System.currentTimeMillis() + ".jpg")
            imageRef.putFile(filepath)
                .addOnSuccessListener { p0 ->
                    pd.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "FileUploadedSucessfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    imageRef.downloadUrl.addOnSuccessListener {
                        val downloadUrl: Uri = it

                        upload = Upload(
                            etName.text.toString().trim(),
                            etAddress.text.toString().trim(),
                            downloadUrl.toString(),
                            etCity.text.toString().trim()
                        )
                        val imageUploadId = mDatabaseRef.push().key.toString()
                        mDatabaseRef.child(imageUploadId).setValue(upload)
                        Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
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

    private fun startFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            filepath = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            imgProduct.setImageBitmap(bitmap)
        }
    }
}

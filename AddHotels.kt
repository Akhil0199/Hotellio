package com.example.hotellio

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class AddHotels : AppCompatActivity(), View.OnClickListener {

     private lateinit var mHotelname: EditText
     private lateinit var mTotalAcRooms: EditText
     private lateinit var mTotalNonAcRooms: EditText
     private lateinit var mAcRoomPrice: EditText
     private lateinit var mNonAcRoomPrice: EditText
     private lateinit var mLocation: EditText
     private lateinit var mFacilities: EditText
     private lateinit var mAddBtn: Button
     private lateinit var imageUrl: String


    private lateinit var db: FirebaseFirestore
    private lateinit var hotelname: String
    private lateinit var totalacrooms: String
    private lateinit var totalnonacrooms: String
    private lateinit var acroomprice: String
    private lateinit var nonacroomprice: String
    private lateinit var location: String
    private lateinit var Facilities: String

    private lateinit var addImageBtn: Button
    private lateinit var uploadBtn: Button
    private lateinit var imageView: ImageView
    private lateinit var filePath : Uri
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_hotels)

        mHotelname=findViewById(R.id.hotelName)
        mTotalAcRooms=findViewById(R.id.totalAcRooms)
        mTotalNonAcRooms=findViewById(R.id.totalNonAcRooms)
        mAcRoomPrice=findViewById(R.id.acRoomPrice)
        mNonAcRoomPrice=findViewById(R.id.nonAcRoomPrice)
        mLocation=findViewById(R.id.location3)
        mFacilities=findViewById(R.id.facilities)
        imageView=findViewById(R.id.imageView)
        uploadBtn=findViewById(R.id.upload)
        addImageBtn=findViewById(R.id.addHotelImage)
        mAddBtn=findViewById(R.id.add)

        storageReference = FirebaseStorage.getInstance().reference

        addImageBtn.setOnClickListener(){
            startFileChooser()
    }
        uploadBtn.setOnClickListener{
            uploadFile()
        }
        mAddBtn.setOnClickListener(this)
}

    private fun uploadFile() {
        val pd=ProgressDialog(this)
        pd.setTitle("Uploading")
        pd.show()

        val randomKey: String = UUID.randomUUID().toString()
        val imageRef:StorageReference = FirebaseStorage.getInstance().reference.child("Hotel Image/"+randomKey)
        val uploadTask= imageRef.putFile(filePath)
            .addOnProgressListener { task ->
                val progress = (100.0 * task.bytesTransferred)/ task.totalByteCount
                pd.setMessage("Uploaded ${progress.toInt()}%")
            }

        uploadTask.continueWithTask(){task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }



    .addOnCompleteListener(){ task ->
        if (task.isSuccessful) {
            pd.dismiss()
            imageUrl = task.getResult().toString()
        }
    }
        .addOnSuccessListener {
            pd.dismiss()
            Toast.makeText(this,"Image Uploaded",Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener{ task ->
            pd.dismiss()
            Toast.makeText(this,task.message,Toast.LENGTH_SHORT).show()
        }

    }

    private fun startFileChooser() {
        val i=Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose image"),111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            filePath = data.data!!
            val imageUri = data.data
            imageView.setImageURI(imageUri)
             imageUrl = imageUri.toString()
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.add -> {
                    val bool: Boolean =
                        validateHotel()
                    if (bool == true)
                        addHotel()
                    else
                        Toast.makeText(
                            applicationContext,
                            "Registration Failed",
                            Toast.LENGTH_LONG
                        ).show()
                }

            }
        }
    }

    private fun addHotel() {
        db = FirebaseFirestore.getInstance()

        val hotelId = db.collection("Hotels").document().id

        val user = hashMapOf(
                "hotelId" to hotelId,
                "Hotel_Name" to hotelname,
                "Total_Ac_Rooms" to totalacrooms,
                "Ac_Room_Price" to acroomprice,
                "Total_Non_Ac_Rooms" to totalnonacrooms,
                "Non_Ac_Room_Price" to nonacroomprice,
                "Location" to location,
                "Facilities" to Facilities,
                "Image_Url" to imageUrl
        )

        db.collection("Hotels").document(hotelId)
            .set(user)

            .addOnCompleteListener{
                imageView.setImageResource(0)
                mHotelname.text.clear()
                mTotalAcRooms.text.clear()
                mAcRoomPrice.text.clear()
                mTotalNonAcRooms.text.clear()
                mNonAcRoomPrice.text.clear()
                mLocation.text.clear()
                mFacilities.text.clear()
            }
            .addOnSuccessListener {
                Toast.makeText(
                    baseContext, "Hotel Added",
                    Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext, "Not Successfull",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun validateHotel(): Boolean {
        hotelname=mHotelname.text.toString()
        totalacrooms=mTotalAcRooms.text.toString()
        totalnonacrooms=mTotalNonAcRooms.text.toString()
        acroomprice=mAcRoomPrice.text.toString()
        nonacroomprice=mNonAcRoomPrice.text.toString()
        location=mLocation.text.toString()
        Facilities=mFacilities.text.toString()



        if (TextUtils.isEmpty(hotelname)) {
            mHotelname.error = "Hotel Name Required"
            mHotelname.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(totalacrooms)) {
            mTotalAcRooms.error = "Required"
            mTotalAcRooms.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(totalnonacrooms)) {
            mTotalNonAcRooms.error = "Required"
            mTotalNonAcRooms.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(location)) {
            mLocation.error = "Required"
            mLocation.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(acroomprice)) {
            mAcRoomPrice.error = "Required"
            mAcRoomPrice.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(nonacroomprice)) {
            mNonAcRoomPrice.error = "Required"
            mNonAcRoomPrice.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(Facilities)) {
            mFacilities.error = "Required"
            mFacilities.requestFocus()
            return false
        }

        return true
    }

    }




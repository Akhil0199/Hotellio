package com.example.hotellio

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class UpdateHotelDetails: AppCompatActivity(), View.OnClickListener {

    private lateinit var image: ImageView
    private lateinit var hotelName: TextView
    private lateinit var location: TextView
    private lateinit var updateTotalAcRooms: EditText
    private lateinit var updateTotalNonAcRooms: EditText
    private lateinit var updateAcRoomPrice: EditText
    private lateinit var updateNonAcRoomPrice: EditText
    private lateinit var updateHotelBtn: Button
    private lateinit var prgBar: ProgressBar
    private lateinit var detail: HotelModel
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_hotel_details)

        image = findViewById(R.id.uploadedImage)
        hotelName = findViewById(R.id.hoteName6)
        location = findViewById(R.id.location6)
        updateTotalAcRooms = findViewById(R.id.updateTotalAcRooms)
        updateTotalNonAcRooms = findViewById(R.id.updateTotalNonAcRooms)
        updateAcRoomPrice = findViewById(R.id.updateAcRoomPrice)
        updateNonAcRoomPrice = findViewById(R.id.updateNonAcRoomPrice)
        prgBar = findViewById(R.id.prgBar)

        updateHotelBtn = findViewById(R.id.updateBtn)

        updateHotelBtn.setOnClickListener(this)

        db = FirebaseFirestore.getInstance()
        detail = intent.getSerializableExtra("updateHotels") as HotelModel

        Glide.with(baseContext).load(detail.Image_Url).into(image)
        hotelName.text = "Hotel Name : ${detail.Hotel_Name}"
        location.text = "Location: ${detail.Location}"
        prgBar.visibility = View.GONE


    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.updateBtn -> {
                var bool: Boolean =
                        validateHotel()
                if (bool == true) {
                    prgBar.visibility = View.VISIBLE
                    var updatedTotalAcRooms = updateTotalAcRooms.text.toString()
                    var updatedTotalNonAcRooms = updateTotalNonAcRooms.text.toString()
                    var updatedAcRoomPrice = updateAcRoomPrice.text.toString()
                    var updatedNonAcRoomPrice = updateNonAcRoomPrice.text.toString()

                val ref = db.collection("Hotels").document(detail.hotelId!!)
                ref
                        .update(mapOf(
                                "Total_Ac_Rooms" to updatedTotalAcRooms,
                                "Total_Non_Ac_Rooms" to updatedTotalNonAcRooms,
                                "Ac_Room_Price" to updatedAcRoomPrice,
                                "Non_Ac_Room_Price" to updatedNonAcRoomPrice
                        ))
                        .addOnSuccessListener {
                            Toast.makeText(this, "Updated successfully", Toast.LENGTH_LONG).show()
                            updateTotalAcRooms.text.clear()
                            updateTotalNonAcRooms.text.clear()
                            updateAcRoomPrice.text.clear()
                            updateNonAcRoomPrice.text.clear()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "${e.message}", Toast.LENGTH_LONG).show()
                        }

                prgBar.visibility = View.GONE
            }}else ->
            Toast.makeText(
                applicationContext,
                "Updation Failed",
                Toast.LENGTH_LONG
        ).show()
        }

    }


    private fun validateHotel(): Boolean {
        if (TextUtils.isEmpty(updateTotalAcRooms.toString())) {
            updateTotalAcRooms.setError("Required")
            updateTotalAcRooms.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(updateTotalNonAcRooms.toString())) {
            updateTotalNonAcRooms.setError("Required")
            updateTotalNonAcRooms.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(updateAcRoomPrice.toString())) {
            updateAcRoomPrice.setError("Required")
            updateAcRoomPrice.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(updateNonAcRoomPrice.toString())) {
            updateNonAcRoomPrice.setError("Required")
            updateNonAcRoomPrice.requestFocus()
            return false
        }
        return true

    }
}
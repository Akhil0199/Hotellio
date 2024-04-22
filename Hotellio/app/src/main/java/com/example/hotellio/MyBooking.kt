package com.example.hotellio

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MyBooking: AppCompatActivity() {

    private var bookingList: ArrayList<MyBookingModel> = ArrayList()
    private val myBookingAdapter: MyBookingAdapter = MyBookingAdapter(bookingList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_booking)
       val recycler_view = findViewById<RecyclerView>(R.id.recycler_View1)

        myBookingAdapter.bookingListItem = bookingList
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = myBookingAdapter

        var firestore: FirebaseFirestore
        var fUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        var emailId1 = fUser?.email
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Hotel Bookings")
                .whereEqualTo("Email",emailId1)
                .get()
                .addOnSuccessListener {

                    if (!it.isEmpty) {
                        val list: List<DocumentSnapshot> = it.documents
                        for (d: DocumentSnapshot in list) {
                            var myBookingModel: MyBookingModel? = d.toObject(MyBookingModel::class.java)
                            bookingList.add(myBookingModel!!)
                        }
                    }
                    myBookingAdapter.notifyDataSetChanged()
                    Log.d("TAG", "hotelsList=$bookingList")
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
    }
}
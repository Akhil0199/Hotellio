package com.example.hotellio

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


private const val TAG: String = "HOMEPAGE_LOG"
class UpdateHotels: AppCompatActivity() {

    lateinit var firestore_list: RecyclerView
    private var hotelsList: ArrayList<HotelModel> = ArrayList<HotelModel>()
    private val hotelListAdapter: HotelListAdapter = HotelListAdapter(hotelsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_hotels)
        firestore_list = findViewById(R.id.firestore_list)

            hotelListAdapter.hotelsListItem = hotelsList
            firestore_list.setHasFixedSize(true)
            firestore_list.layoutManager = LinearLayoutManager(this)
            firestore_list.adapter = hotelListAdapter

            var firestore: FirebaseFirestore
            firestore = FirebaseFirestore.getInstance()
            firestore.collection("Hotels")
                    .get()
                    .addOnSuccessListener {

                        if (!it.isEmpty) {
                            val list: List<DocumentSnapshot> = it.documents
                            for (d: DocumentSnapshot in list) {
                                var hotelModel: HotelModel? = d.toObject(HotelModel::class.java)
                                hotelsList.add(hotelModel!!)
                            }
                        }
                        hotelListAdapter.notifyDataSetChanged()
                        Log.d(TAG, "hotelsList=$hotelsList")
                    }
                    .addOnFailureListener {
                        it.printStackTrace()
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }

        }


    }

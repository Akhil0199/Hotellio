package com.example.hotellio

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ViewRecords: AppCompatActivity(), SearchView.OnQueryTextListener {

    private var bookingList: ArrayList<RecordsModel> = ArrayList()
    private val viewRecordsAdapter: ViewRecordsAdapter = ViewRecordsAdapter(bookingList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_records)
        val recycler_view = findViewById<RecyclerView>(R.id.recycler_view2)


        viewRecordsAdapter.allBookingsList = bookingList
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = viewRecordsAdapter

        var firestore: FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Hotel Bookings")
                .get()
                .addOnSuccessListener {

                    if (!it.isEmpty) {
                        val list: List<DocumentSnapshot> = it.documents
                        for (d: DocumentSnapshot in list) {
                            var recordsModel: RecordsModel? = d.toObject(RecordsModel::class.java)
                            bookingList.add(recordsModel!!)
                        }
                    }
                    viewRecordsAdapter.notifyDataSetChanged()
                    Log.d("TAG", "hotelsList=$bookingList")
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        return true
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            filter(newText)
        }
        return false
    }
    private fun filter(text:String)//the data that we want to filter from the recycler view
    {
        var filteredList = ArrayList<RecordsModel>()
        for (item : RecordsModel in bookingList)
            if (item.Location.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        for (item : RecordsModel in bookingList)
            if (item.Hotel_Name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        for (item : RecordsModel in bookingList)
            if (item.Email.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            viewRecordsAdapter.filterList(filteredList);
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.actionSearch1 -> {
                var searchView: SearchView = item.actionView as SearchView
                searchView.queryHint = "Hotel Name, Location or Email"
                searchView.setOnQueryTextListener(this)
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

}

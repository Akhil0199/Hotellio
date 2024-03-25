package com.example.hotellio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class CustomerDashBoard: AppCompatActivity(),SearchView.OnQueryTextListener {



    lateinit var recycler_view: RecyclerView
    private var hotelsList: ArrayList<CustomerModel> = ArrayList<CustomerModel>()
    private val customerDashbordAdapter: CustomerDashbordAdapter = CustomerDashbordAdapter(hotelsList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_dashboard)

        recycler_view = findViewById(R.id.recycler_view)
        customerDashbordAdapter.hotelsListItem = hotelsList
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = customerDashbordAdapter


        var firestore: FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Hotels")
                .get()
                .addOnSuccessListener {

                    if (!it.isEmpty) {
                        val list: List<DocumentSnapshot> = it.documents
                        for (d: DocumentSnapshot in list) {
                            var customerModel: CustomerModel? = d.toObject(CustomerModel::class.java)
                            hotelsList.add(customerModel!!)
                        }
                    }
                    customerDashbordAdapter.notifyDataSetChanged()
                    Log.d("TAG", "hotelsList=$hotelsList")
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
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
    private fun filter(text:String)
    {
        var filteredList = ArrayList<CustomerModel>()
        for (item : CustomerModel in hotelsList)
            if (item.Location.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

        for (item : CustomerModel in hotelsList)
            if (item.Hotel_Name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
           customerDashbordAdapter.filterList(filteredList);
        }
    }


    override fun onBackPressed() {
        finish()
        System.exit(0)
    }



    val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.actionSearch ->{
                val searchView : SearchView = item.actionView as SearchView
                searchView.queryHint = "Hotel Name or Location"
                searchView.setOnQueryTextListener(this)
                true
            }


            R.id.item->{
                val intent = Intent(this, MyBooking::class.java)
                startActivity(intent)
                true
            }
            R.id.item1->{
                fAuth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this,"Logout Successful", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}


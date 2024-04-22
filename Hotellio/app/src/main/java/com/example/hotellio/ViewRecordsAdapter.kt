package com.example.hotellio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewRecordsAdapter(var allBookingsList: List<RecordsModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

     class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(recordsModel: RecordsModel) {
            val hotelName = itemView.findViewById<TextView>(R.id.hotelName9)
            val location = itemView.findViewById<TextView>(R.id.location9)
            val typeofroom = itemView.findViewById<TextView>(R.id.typeofroom9)
            val noofrooms = itemView.findViewById<TextView>(R.id.noofrooms9)
            val checkin = itemView.findViewById<TextView>(R.id.checkin9)
            val checkout = itemView.findViewById<TextView>(R.id.checkout9)
            val CustomerEmail = itemView.findViewById<TextView>(R.id.customermail)

            hotelName.text = recordsModel.Hotel_Name
            location.text = recordsModel.Location
            typeofroom.text = "Type Of Rooms : " + recordsModel.Type_Of_Room
            noofrooms.text = "No Of Rooms : " + recordsModel.No_Of_Rooms
            checkin.text = "Check-In: " + recordsModel.Check_In
            checkout.text = "Check-Out: " + recordsModel.Check_Out
            CustomerEmail.text = recordsModel.Email
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_all_bookings, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(allBookingsList[position])


    }

    override fun getItemCount(): Int {
        return allBookingsList.size
    }
    fun filterList(filterList: ArrayList<RecordsModel>) {
        allBookingsList = filterList
        notifyDataSetChanged()
    }


}
package com.example.hotellio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class MyBookingAdapter(var bookingListItem: List<MyBookingModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    public class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val cancelBooking = itemView.findViewById<Button>(R.id.cancelBooking)
        val image = itemView.findViewById<ImageView>(R.id.hotelImage8)
        val hotelName = itemView.findViewById<TextView>(R.id.hotelName8)
        val location = itemView.findViewById<TextView> (R.id.location8)
        val typeofroom = itemView.findViewById<TextView>(R.id.typeOfRoom8)
        val noofrooms = itemView.findViewById<TextView>(R.id.noOfRooms8)
        val checkin = itemView.findViewById<TextView>(R.id.checkIn8)
        val checkout = itemView.findViewById<TextView>(R.id.checkOut8)

        fun bind(myBookingModel: MyBookingModel){


            hotelName.text=myBookingModel.Hotel_Name
            location.text=myBookingModel.Location
            typeofroom.text="Type Of Rooms : " +myBookingModel.Type_Of_Room
            noofrooms.text="No Of Rooms : " +myBookingModel.No_Of_Rooms
            checkin.text="Check-In: " +myBookingModel.Check_In
            checkout.text="Check-Out: " +myBookingModel.Check_Out


            Glide.with(itemView.context)
                    .load(myBookingModel.Hotel_Image).into(image)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booking_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(bookingListItem[position])

        val bookings: MyBookingModel = bookingListItem[position]
        holder.cancelBooking?.setOnClickListener() {
            openDialog(bookings, it)
        }

    }

    private fun openDialog(bookings: MyBookingModel, view: View?) {
        val dialogView = LayoutInflater.from(view?.context).inflate(R.layout.delete_dialog, null)
        val builder = view.let { it1 ->
            AlertDialog.Builder(it1!!.context)
                    .setView(dialogView)
                    .setTitle("Cancel Booking")
    }
        val mAlertDialog = builder.show()

        val yes = dialogView.findViewById<Button>(R.id.yesBtn)
        val no = dialogView.findViewById<Button>(R.id.noBtn)

        yes.setOnClickListener {
            mAlertDialog.dismiss()
            var db : FirebaseFirestore = FirebaseFirestore.getInstance()
            db.collection("Hotel Bookings").document(bookings.BookingId).delete()
                    .addOnSuccessListener {
                        Toast.makeText(view?.context, "Booking Cancelled", Toast.LENGTH_LONG).show() }
                    .addOnFailureListener {
                        Toast.makeText(view?.context, "Booking Not Cancelled", Toast.LENGTH_LONG).show() }
        }
        no.setOnClickListener {
            mAlertDialog.dismiss()
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bookingListItem.size
    }

}
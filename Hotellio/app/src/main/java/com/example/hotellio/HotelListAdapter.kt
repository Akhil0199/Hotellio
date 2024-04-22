package com.example.hotellio

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


open class HotelListAdapter(var hotelsListItem: List<HotelModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val delete = itemView.findViewById<ImageButton>(R.id.deleteButton)
         val edit = itemView.findViewById<ImageButton>(R.id.editButton)

        fun bind(hotelModel: HotelModel) {
            val hotelName1 = itemView.findViewById<TextView>(R.id.hotelName1)
            val location = itemView.findViewById<TextView>(R.id.location1)
            hotelName1.text = hotelModel.Hotel_Name
            location.text = hotelModel.Location

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotels_list, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(hotelsListItem[position])
        val hotels: HotelModel = hotelsListItem[position]
        holder.delete?.setOnClickListener() {
            openDialog(hotels, it)
        }

        holder.edit?.setOnClickListener() {
            val intent = Intent(it?.context, UpdateHotelDetails::class.java)
            intent.putExtra("updateHotels", hotels)
            it?.context?.startActivity(intent)
        }

    }

    private fun openDialog(hotels: HotelModel, view: View) {
        val dialogView = LayoutInflater.from(view.context).inflate(R.layout.delete_dialog, null)
        val builder = view.let { it1 ->
            AlertDialog.Builder(it1.context)
                .setView(dialogView)
                .setTitle("Delete")
        }
        val mAlertDialog = builder.show()

        val yes = dialogView.findViewById<Button>(R.id.yesBtn)
        val no = dialogView.findViewById<Button>(R.id.noBtn)

        yes.setOnClickListener {
            mAlertDialog.dismiss()
            val db : FirebaseFirestore = FirebaseFirestore.getInstance()
            db.collection("Hotels").document(hotels.hotelId).delete()
                    .addOnSuccessListener {
                        Toast.makeText(view.context, "deleted successfully", Toast.LENGTH_LONG).show() }
                    .addOnFailureListener {
                        Toast.makeText(view.context, "deleted", Toast.LENGTH_LONG).show() }
        }
        no.setOnClickListener {
            mAlertDialog.dismiss()
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return hotelsListItem.size
    }

}


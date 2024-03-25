package com.example.hotellio

import android.content.Intent
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class CustomerDashbordAdapter(var hotelsListItem: List<CustomerModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    public class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val bookBtn: Button = itemView.findViewById(R.id.cancelBooking)
        fun bind(customerModel: CustomerModel){
            val hotelName2 = itemView.findViewById<TextView>(R.id.hotelName2)
            val location2 = itemView.findViewById<TextView> (R.id.location2)
            val facilities = itemView.findViewById<TextView>(R.id.facilities1)
            hotelName2.text=customerModel.Hotel_Name
            location2.text=customerModel.Location
            facilities.text=customerModel.Facilities


            val imageView = itemView.findViewById<ImageView>(R.id.hotelImage8)
            Glide.with(itemView.context)
                    .load(customerModel.Image_Url).into(imageView)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(hotelsListItem[position])
        val details: CustomerModel = hotelsListItem[position]

        holder.bookBtn.setOnClickListener(){
            val intent = Intent(it?.context, BookingPage::class.java)
            intent.putExtra("BookHotel",details)
            it?.context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return hotelsListItem.size
    }

    fun filterList(filterList: ArrayList<CustomerModel>) {
        hotelsListItem = filterList
        notifyDataSetChanged()
    }
}
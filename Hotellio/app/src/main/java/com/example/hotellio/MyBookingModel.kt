package com.example.hotellio

import java.io.Serializable

data class MyBookingModel (
        val Email : String = "",
        val BookingId : String = "",
        val Hotel_Image : String = "",
        val Hotel_Name : String = "",
        val Location : String = "",
        val Type_Of_Room : String = "",
        val No_Of_Rooms : String = "",
        val Check_In : String = "",
        val Check_Out : String = ""
        ): Serializable
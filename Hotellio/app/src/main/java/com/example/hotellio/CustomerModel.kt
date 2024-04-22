package com.example.hotellio

import java.io.Serializable

data class CustomerModel(
        val Rating: Float = 0.0f,
        val Facilities: String = "",
        val No_Of_Ac_Rooms: String = "",
        val No_Of_Non_Ac_Rooms: String = "",
        var hotelId : String = "",
        var Hotel_Name : String="",
        var Suite_Price : String = "",
        var Single_room_Price : String = "",
        var Location: String="",
        var Image_Url: String = ""
): Serializable
package com.example.hotellio

import java.io.Serializable

data class HotelModel(
        var hotelId: String ="",
        var Hotel_Name: String ="",
        var No_Of_Ac_Rooms: String = "",
        var No_Of_Non_Ac_Rooms: String="",
        var Ac_Room_Price : String = "",
        var Non_Ac_Room_Price : String = "",
        var Location: String="",
        var Image_Url: String=""
     ): Serializable
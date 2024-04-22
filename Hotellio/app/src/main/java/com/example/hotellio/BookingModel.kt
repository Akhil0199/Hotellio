package com.example.hotellio

import java.io.Serializable

data class BookingModel(
        val name: String = "",
        val phone: String = "",
        val email: String = ""
        ): Serializable
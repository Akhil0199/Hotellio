package com.example.hotellio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BookingDone: AppCompatActivity(), View.OnClickListener {

    private lateinit var goToDashboard: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking_confirmed)

        goToDashboard = findViewById(R.id.goToDashBoard)
        goToDashboard.setOnClickListener(this)
    }

    override fun onBackPressed() {
        finish()
       val intent = Intent(this, CustomerDashbordAdapter::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.goToDashBoard -> {
                    val intent = Intent(this, CustomerDashBoard::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
package com.example.hotellio

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminDashboard: AppCompatActivity(), View.OnClickListener {
    lateinit var mAddHotelsBtn: Button
    lateinit var mUpdateHotelsBtn: Button
    lateinit var mViewRecords: Button
    lateinit var mLogoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_dashboard)

        mUpdateHotelsBtn=findViewById(R.id.updateHotels)
        mAddHotelsBtn = findViewById(R.id.addHotels)
        mLogoutBtn = findViewById(R.id.adminLogout)
        mViewRecords = findViewById(R.id.viewAllBookings)

        mAddHotelsBtn.setOnClickListener(this)
        mUpdateHotelsBtn.setOnClickListener(this)
        mViewRecords.setOnClickListener(this)
        mLogoutBtn.setOnClickListener(this)
    }

    val fAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.addHotels -> {
                    val intent = Intent(this, AddHotels::class.java)
                    startActivity(intent)
                }
                R.id.updateHotels -> {
                    val intent = Intent(this, UpdateHotels::class.java)
                    startActivity(intent)
                }
                R.id.viewAllBookings -> {
                    val intent = Intent(this, ViewRecords::class.java)
                    startActivity(intent)
                }

                R.id.adminLogout -> {
                    fAuth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this,"Logout Successful", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
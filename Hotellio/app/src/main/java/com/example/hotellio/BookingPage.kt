package com.example.hotellio

import android.content.Intent
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.util.*
import kotlin.collections.ArrayList


class BookingPage: AppCompatActivity(), View.OnClickListener {

    val cal:Calendar= Calendar.getInstance()

    private lateinit var hotelName3: TextView
    private lateinit var acRoomPrice1: TextView
    private lateinit var nonAcRoomPrice1: TextView
    private lateinit var personName: TextView
    private lateinit var mobileNumber: TextView
    private lateinit var location: TextView
    private lateinit var roomTypeSpin: Spinner
    private lateinit var noOfRooms: EditText
    private lateinit var checkIn: EditText
    private lateinit var checkOut: EditText
    private lateinit var bookBtn: Button
    private lateinit var detail: CustomerModel
    private lateinit var emailid: TextView
    private lateinit var fStore: FirebaseFirestore

    private lateinit var roomtype: String
    private lateinit var noofrooms: String
    private lateinit var checkin: String
    private lateinit var checkout: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking_page1)

        hotelName3 = findViewById(R.id.hotelName5)
        acRoomPrice1 = findViewById(R.id.acRoomPrice5)
        nonAcRoomPrice1 = findViewById(R.id.nonAcRoomPrice5)
        personName = findViewById(R.id.personName5)
        mobileNumber = findViewById(R.id.mobileNumber5)
        location = findViewById(R.id.location5)
        roomTypeSpin = findViewById(R.id.spinner3)
        noOfRooms = findViewById(R.id.selectNoOfRooms)
        checkIn = findViewById(R.id.checkInDate)
        checkOut = findViewById(R.id.checkOutDate)
        bookBtn = findViewById(R.id.bookBtn)
        emailid = findViewById(R.id.emailid)
        check_in()
        check_out()

        detail= intent.getSerializableExtra("BookHotel") as CustomerModel



        setData()

        bookBtn.setOnClickListener(this)

        ArrayAdapter.createFromResource(
                this,
                R.array.roomType_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roomTypeSpin.adapter = adapter
        }


        val fUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        fStore = FirebaseFirestore.getInstance()
        val emailId = fUser?.email

        ArrayList<User>()
        fStore.collection("users").whereEqualTo("email",emailId).get()
                .addOnSuccessListener {
                    for (document in it){
                        val user = document.toObject(BookingModel::class.java)
                        personName.text = user.name
                        mobileNumber.text = user.phone
                        emailid.text = user.email

                    }
                }
    }

    private fun setData() {
        hotelName3.text =detail.Hotel_Name
        location.text = detail.Location
        acRoomPrice1.text =    "Suite Price : $" +detail.Suite_Price
        nonAcRoomPrice1.text = "Single Room Price : $" +detail.Single_room_Price

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.bookBtn -> {
                    var bool: Boolean =
                            validateHotel()
                    if (bool == true)
                        addBooking()
                    else
                        Toast.makeText(
                                applicationContext,
                                "Registration Failed",
                                Toast.LENGTH_LONG
                        ).show()
                }

            }
        }
    }

    private fun addBooking() {
        var fUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        fStore = FirebaseFirestore.getInstance()
        var emailId1 = fUser?.email
       val db = FirebaseFirestore.getInstance()

       val BookingId =  db.collection("Hotel Bookings").document().id
        val user = hashMapOf(
                "BookingId" to BookingId,
                "Hotel_Image" to detail.Image_Url,
                "Hotel_Name" to detail.Hotel_Name,
                "Location" to detail.Location,
                "Type_Of_Room" to roomtype,
                "No_Of_Rooms" to noofrooms,
                "Email" to emailId1,
                "Check_In" to checkin,
                "Check_Out" to checkout,
        )
                db.collection("Hotel Bookings").document(BookingId)
                        .set(user)
                .addOnSuccessListener {
                    db.collection("Hotels").get()
                          .addOnSuccessListener {

                            }

                    db.collection("Hotels")
                    val intent = Intent(this, BookingDone::class.java)
                    startActivity(intent)
                }
    }

    private fun check_in() {
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                    view: DatePicker, year: Int, monthOfYear: Int,
                    dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView1()
            }
        }

        checkIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                        this@BookingPage,
                        dateSetListener,
                                               cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })
    }
    private fun check_out(){
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                    view: DatePicker, year: Int, monthOfYear: Int,
                    dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView2()
            }
        }

        checkOut.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                        this@BookingPage,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        })
    }

    private fun updateDateInView1() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        (checkIn as TextView).text = sdf.format(cal.getTime())
    }
    private fun updateDateInView2() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        (checkOut as TextView).text = sdf.format(cal.getTime())
    }

    private fun validateHotel(): Boolean {
        roomtype = roomTypeSpin.selectedItem.toString()
        noofrooms = noOfRooms.text.toString()
        checkin = checkIn.text.toString()
        checkout = checkOut.text.toString()


        if (TextUtils.isEmpty(noofrooms)) {
            noOfRooms.error = "Required"
            noOfRooms.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(checkin)) {
            checkIn.error = "Required"
            checkIn.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(checkout)) {
            checkOut.error = "Required"
            checkOut.requestFocus()
            return false
        }

        return true
    }
    }


















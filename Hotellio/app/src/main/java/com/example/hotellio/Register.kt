package com.example.hotellio

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.appcompat.app.AppCompatActivity

class Register: AppCompatActivity() ,View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var positionSpin: Spinner
    private lateinit var position_array: Array<String>
    private lateinit var position1: String
    private lateinit var mEmail: EditText
    private lateinit var mPassword: EditText
    private lateinit var mFullname: EditText
    private lateinit var mPhone: EditText
    private lateinit var mRegister: Button
    private lateinit var mProgressBar: ProgressBar


    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var fullname: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var phone: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        position_array = resources.getStringArray(R.array.position_array)
        positionSpin = findViewById(R.id.spinner2)
        mEmail = findViewById(R.id.email1)
        mPassword = findViewById(R.id.password1)
        mFullname = findViewById(R.id.email)
        mPhone = findViewById(R.id.phone)
        mRegister = findViewById(R.id.register1)
        mProgressBar = findViewById(R.id.progressBar3)
        mAuth = FirebaseAuth.getInstance()

        ArrayAdapter.createFromResource(
            this,
            R.array.position_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            positionSpin.adapter = adapter
        }
        positionSpin.onItemSelectedListener = this
        mRegister.setOnClickListener(this)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        position1 = position_array[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.register1 -> {
                    var bool: Boolean =
                        validateUser()
                    if (bool == true)
                        registerUser()
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

    fun registerUser() {
        mProgressBar.setVisibility(View.VISIBLE)
        mAuth.createUserWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext, "Registered Successfuly",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    db = FirebaseFirestore.getInstance()
                    val user = hashMapOf(
                        "name" to fullname,
                        "email" to email,
                        "phone" to phone,
                        "position" to position1
                    )
                    db.collection("users")
                        .add(user)

                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(
                                baseContext, "User Details Added",
                                Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                baseContext, "Not Successfull",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        baseContext, task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    mProgressBar.setVisibility(View.GONE)
                }
            }
    }

    fun validateUser(): Boolean {
        fullname = mFullname.text.toString()
        email = mEmail.text.toString()
        password = mPassword.text.toString()
        phone = mPhone.text.toString()
        val pattern: String = "^[a-zA-Z0-9]+@(gmail.com|hotellio.org)$"


        if (TextUtils.isEmpty(fullname)) {
            mFullname.error = "Name Required"
            mFullname.requestFocus()
            return false
        }
        if (TextUtils.isEmpty(email)) {
            mEmail.error = "Email Required"
            mEmail.requestFocus()
            return false
        }
        if (!email.matches(pattern.toRegex())){
            mEmail.error = "Invalid Email"
            mEmail.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            mPassword.error = "Password Required"
            mPassword.requestFocus()
            return false
        }

        if (password.length < 8 || password.length > 16) {
            mPassword.error = "Atleast 8 characters required"
            mPassword.requestFocus()
            return false
        }

        if (TextUtils.isEmpty(phone)) {
            mPhone.error = "Phone Required"
            mPhone.requestFocus()
            return false
        }

        return true
    }

}






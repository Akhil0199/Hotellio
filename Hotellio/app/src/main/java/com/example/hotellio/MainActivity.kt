package com.example.hotellio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    lateinit var mLoginBtn: Button
    lateinit var mRegisterBtn: Button
    lateinit var position1: String
    lateinit var positionSpin: Spinner
    lateinit var positions_array: Array<String>
    lateinit var mEmail: EditText
    lateinit var mPassword: EditText
    lateinit var mProgressBar: ProgressBar

    private var backPressedTime = 0L

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var position: String
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        positions_array = resources.getStringArray(R.array.positions_array)
        positionSpin = findViewById(R.id.spinner2)
        mEmail = findViewById(R.id.email)
        mPassword = findViewById(R.id.password1)
        mProgressBar = findViewById(R.id.progressBar)
        mProgressBar.visibility = View.GONE
        mAuth = FirebaseAuth.getInstance()

        mLoginBtn = findViewById(R.id.login)
        mRegisterBtn = findViewById(R.id.register1)


        if (supportActionBar != null)
            supportActionBar?.hide()



        ArrayAdapter.createFromResource(
            this,
            R.array.positions_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            positionSpin.adapter = adapter
        }
        positionSpin.onItemSelectedListener = this
        mRegisterBtn.setOnClickListener(this)
        mLoginBtn.setOnClickListener(this)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        position1 = positions_array[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(applicationContext, "Press Back Again To Exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.login ->{
                    val bool:Boolean =validateUser()
                    if(bool == true) {
                        loginUser()
                    }
                    else
                        Toast.makeText(applicationContext,"Login unsuccesfull",Toast.LENGTH_LONG).show()
                }
                R.id.register1 ->{
                    val intent = Intent(this, Register::class.java) //Redirecting to register screen
                    startActivity(intent)}
            }
        }
    }
    fun validateUser() : Boolean  {
        email = mEmail.text.toString()
        password = mPassword.text.toString()

        if(TextUtils.isEmpty(email))
        {
            mEmail.error = "Email Required"
            mEmail.requestFocus()
            return false
        }

        if(TextUtils.isEmpty(password))
        {
            mPassword.error = "Password Required"
            mPassword.requestFocus()
            return false
        }
        if(password.length<8 || password.length>16)
        {
            mPassword.error = "8 - 16 characters required"
            mPassword.requestFocus()
            return false
        }
        return true
    }
    private fun loginUser()
    {
        mProgressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (email.contains("@gmail.com") && position1.equals("Customer") && task.isSuccessful) {
                    mProgressBar.visibility = View.GONE
                    val intent=Intent(this, CustomerDashBoard::class.java)
                    startActivity(intent)

                }
                if (email.contains("@hotellio.org") && position1.equals("Employee") && task.isSuccessful) {
                    mProgressBar.visibility = View.GONE
                    val intent=Intent(this, CustomerDashBoard::class.java)
                    startActivity(intent) }

                if (email.contains("@hotellio.org.us") && position1.equals("Admin") && task.isSuccessful) {
                    mProgressBar.visibility = View.GONE
                    val intent=Intent(this, AdminDashboard::class.java)
                    startActivity(intent)
                }
            } .addOnFailureListener(this){
                    mProgressBar.visibility = View.GONE
                Toast.makeText(this,"Invalid Username and Password",Toast.LENGTH_SHORT).show()
                }
    }

}









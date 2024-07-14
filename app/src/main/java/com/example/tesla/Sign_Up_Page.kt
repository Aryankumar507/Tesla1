package com.example.tesla

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tesla.databinding.ActivitySignUpPageBinding
import com.google.firebase.auth.FirebaseAuth

class Sign_Up_Page : AppCompatActivity() {
    private val binding:ActivitySignUpPageBinding by lazy {
        ActivitySignUpPageBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize Firebase auth
        auth = FirebaseAuth.getInstance()
        binding.signIn.setOnClickListener {
            val intent = Intent(this, Sign_In_Page::class.java)
            startActivity(intent)
            finish()
            }

            binding.registrationButton.setOnClickListener {
                // get text from Edit text
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()

                // check if any field is blank
                if(email.isEmpty()||password.isEmpty()){
                    Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
                }
                else{
                      auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(this){task ->
                            if(task.isSuccessful){
                                Toast.makeText(this,"Registration Succesfull",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,Sign_In_Page::class.java))
                                finish()
                            }
                            else {
                                Toast.makeText(this,"Registration Failed:${task.exception?.message}",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }


        }

}
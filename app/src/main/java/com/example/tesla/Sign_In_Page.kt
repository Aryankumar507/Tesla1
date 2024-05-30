package com.example.tesla

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tesla.databinding.ActivitySignInPageBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.zip.Inflater

class Sign_In_Page : AppCompatActivity() {
    private val binding : ActivitySignInPageBinding by lazy {
        ActivitySignInPageBinding.inflate(layoutInflater)
    }
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        // Initialize firebase auth
        auth = FirebaseAuth.getInstance()
        binding.signUp.setOnClickListener {
            val intent = Intent(this,Sign_Up_Page::class.java)
            startActivity(intent)
            finish()
        }
        binding.SignIn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password1.text.toString()

            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Fill All The Details",Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{task->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Succesfull",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        }
                        else{
                            Toast.makeText(this,"Sign In Failed:${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
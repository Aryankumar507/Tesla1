package com.example.tesla

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tesla.databinding.ActivitySignInPageBinding
import java.util.zip.Inflater

class Sign_In_Page : AppCompatActivity() {
    private val binding : ActivitySignInPageBinding by lazy {
        ActivitySignInPageBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            val intent = Intent(this,Sign_Up_Page::class.java)
            startActivity(intent)
            finish()
        }

    }
}
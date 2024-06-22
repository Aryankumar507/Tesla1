package com.example.tesla

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tesla.databinding.ActivityAddImagesBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class Add_Images : AppCompatActivity() {
    private lateinit var binding: ActivityAddImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddImagesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.uploadImage.setOnClickListener {
            val intent = Intent()
             intent.action = Intent.ACTION_PICK
             intent.type = "image/*"
             imageLauncher.launch(intent)
        }
    }
    val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            if(it.data != null){
               val ref = Firebase.storage.reference.child("photo")
                ref.putFile(it.data!!.data!!)
                ref.downloadUrl.addOnSuccessListener {
                   // binding.showimage.setImageURI(it)
                   Picasso.get().load(it.toString()).into(binding.showimage)
                    Toast.makeText(this,"succes",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show()
        }
    }
}
package com.example.tesla

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tesla.databinding.ActivityAddImagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Add_Images : AppCompatActivity() {
    private lateinit var binding: ActivityAddImagesBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddImagesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference



        binding.uploadImage.setOnClickListener {
            val intent = Intent()
             intent.action = Intent.ACTION_PICK
             intent.type = "image/*"
             imageLauncher.launch(intent)
        }

    }
    val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it ->
        if(it.resultCode == Activity.RESULT_OK){
            if(it.data != null){
                val currentuser = auth.currentUser
                currentuser?.let {
                    user->

                        database.child("users").child(user.uid).child("photo").push().setValue(it.toString())

                }
               val ref = Firebase.storage.reference.child("Photo/"+System.currentTimeMillis()+"."+getFileType(it.data!!.data))
                ref.putFile(it.data!!.data!!)
                ref.downloadUrl.addOnSuccessListener {
                   // binding.showimage.setImageURI(it)
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

    private fun getFileType(data: Uri?): String? {
        val r = contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getMimeTypeFromExtension(r.getType(data!!))
    }



     private fun fetchImageUrls() = CoroutineScope(Dispatchers.IO).launch {
        try {
             val user = auth.currentUser
             val photoRef = user?.let { Firebase.database.reference.child("users").child(it.uid).child("photo")}
             val dataSnapshot = photoRef?.get()?.await()
             val urls = dataSnapshot?.children?.mapNotNull { it.getValue(String::class.java) }

             withContext(Dispatchers.Main){
                 val imagesAdapter = urls?.let { Add_Images_Adapter(it) }
                 binding.imageRecyclerView.adapter = imagesAdapter
                 binding.imageRecyclerView.layoutManager = LinearLayoutManager(this@Add_Images)
             }
            }
        catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@Add_Images,e.message,Toast.LENGTH_SHORT).show()
            }
        }
    }


}
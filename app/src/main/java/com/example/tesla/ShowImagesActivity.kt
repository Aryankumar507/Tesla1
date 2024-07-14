package com.example.tesla

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ShowImagesActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var imagesAdapter: Add_Images_Adapter
    private lateinit var images : MutableList<UserImage>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_images)
        auth = FirebaseAuth.getInstance()
        recyclerView = findViewById(R.id.imageRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        images = mutableListOf()
        imagesAdapter = Add_Images_Adapter(this,images)
        recyclerView.adapter = imagesAdapter
        val currentuser = auth.currentUser

        if (currentuser != null) {
            database = FirebaseDatabase.getInstance().getReference().child("user").child(currentuser.uid).child("photo")
        }
        loadImages()
    }

    private fun loadImages() {
        lifecycleScope.launch {
            try {
                val snapshot = withContext(Dispatchers.IO){
                    database.get().await()
                }
                val templist = mutableListOf<UserImage>()
                for (postSnapshot in snapshot.children) {
                    val userImage = postSnapshot.getValue<UserImage>()
                    userImage?.let {
                        templist.add(it)
                    }
                }
                withContext(Dispatchers.Main){
                    images.clear()
                    images.addAll(templist)
                    imagesAdapter.notifyDataSetChanged()
                }
            }
            catch (e:Exception){
                Toast.makeText(this@ShowImagesActivity,e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
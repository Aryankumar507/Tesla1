package com.example.tesla

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tesla.databinding.ActivityAddImagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.OnProgressListener
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
    private lateinit var mediaController: MediaController
    private lateinit var  progressDialog : ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddImagesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        progressDialog = ProgressDialog(this)






        binding.uploadImage.setOnClickListener {
            val intent = Intent()
             intent.action = Intent.ACTION_PICK
             intent.type = "image/*"
             imageLauncher.launch(intent)
        }

        binding.uploadVideo.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "video/*"
            videoLauncher.launch(intent)
        }

    }


    val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ it ->
        if(it.resultCode == Activity.RESULT_OK){
            if(it.data != null){


                 val currentuserId = auth.currentUser
                currentuserId?.let {
                    user->

                        database.child("users").child(user.uid).child("photo").push().setValue(it.toString())

                }


               val ref = Firebase.storage.reference.child("Photo/"+System.currentTimeMillis()+"."+getFileType(it.data!!.data))
                ref.putFile(it.data!!.data!!)


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


    val videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                progressDialog.setTitle("Uploading...")
                progressDialog.show()


           val ref = Firebase.storage.reference.child("video/"+System.currentTimeMillis()+"."+getFileType(it.data!!.data))
                ref.putFile(it.data!!.data!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        Firebase.database.reference.child("video").push().setValue(it.toString())
                        progressDialog.dismiss()
                        Toast.makeText(this@Add_Images,"Video Uploded",Toast.LENGTH_SHORT).show()
                        val mediaController = MediaController(this)
                        mediaController.setAnchorView(binding.videoView2)
                        binding.videoView2.setVideoURI(it)
                        binding.videoView2.setMediaController(mediaController)
                        binding.videoView2.start()
                        binding.videoView2.setOnCompletionListener {
                            ref.delete().addOnSuccessListener {
                                Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                    .addOnProgressListener {
                        val value = (it.bytesTransferred/it.totalByteCount)*100
                        progressDialog.setTitle("Uploaded"+value.toString()+"%")
                    }
            }
        }
    }





/*
*  private fun fetchImageUrls() = CoroutineScope(Dispatchers.IO).launch {
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
* */



}
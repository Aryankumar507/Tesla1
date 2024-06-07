package com.example.tesla

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tesla.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.Reference

class AddNote : AppCompatActivity() {
    private val binding:ActivityAddNoteBinding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }
    //taking refrence of database
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        //Intialise Firebase Auth
        auth = FirebaseAuth.getInstance()
        //Intialise Firebase database refrence
        databaseReference = FirebaseDatabase.getInstance().reference
        binding.notesSave.setOnClickListener {
            // get text from edit text
            val title = binding.editTextTitle.text.toString()
            val description = binding.editTextDescription.text.toString()

            if (title.isEmpty()&&description.isEmpty()){
                Toast.makeText(this,"Fill both Field",Toast.LENGTH_SHORT).show()
        }
            else{
                val currentUser = auth.currentUser
                currentUser?.let { user->
                    //Generate a unique key for the notes
                    val noteKey = databaseReference.child("users").child(user.uid).child("notes").push().key
                    // note item instance
                    val noteItem = NoteItem(title,description)
                    if (noteKey!= null)
                        //addNotes to the user note
                        databaseReference.child("users").child(user.uid).child("notes").child(noteKey).setValue(noteItem)
                            .addOnCompleteListener{task ->
                                if (task.isSuccessful){
                                    Toast.makeText(this,"Note Save is Succesfull",Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else{
                                    Toast.makeText(this,"Failed to Save Note",Toast.LENGTH_SHORT).show()
                                }
                            }
                }

            }
        }
    }
}
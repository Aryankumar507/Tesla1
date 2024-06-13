package com.example.tesla

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tesla.databinding.ActivityAllNotesBinding
import com.example.tesla.databinding.DilalogUpdateNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllNotes : AppCompatActivity(),NoteAdapter.OnItemClickListener {
    private val binding:ActivityAllNotesBinding by lazy {
        ActivityAllNotesBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //Intialise
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        recyclerView = binding.notes
        recyclerView.layoutManager = LinearLayoutManager(this)

        val currentUser = auth.currentUser
        currentUser?.let {
            user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   val noteList = mutableListOf<NoteItem>()
                    for (noteSnapshot in snapshot.children){
                        val note = noteSnapshot.getValue(NoteItem::class.java)
                        note?.let {
                            noteList.add(it)
                        }
                    }
                    val adapter = NoteAdapter(noteList,this@AllNotes)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    override fun onDeleteClick(noteId: String) {
        val currentUser = auth.currentUser
        currentUser?.let {user->
            val noteRefrence = databaseReference.child("users").child(user.uid).child("notes")
            noteRefrence.child(noteId).removeValue()
        }
    }

    override fun onUpdateClick(noteId: String,currentTitle:String,currentDescription:String) {
        val dialogBinding = DilalogUpdateNotesBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Upadte Notes")
            .setPositiveButton("Update"){dialog,_ ->
                val newTitle = dialogBinding.updateTitle.text.toString()
                val newDescription = dialogBinding.updateDescription.text.toString()
                updateNoteDatabase(noteId,newTitle,newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("Cancle"){
                dialog,_->
                dialog.dismiss()
            }
            .create()
        dialogBinding.updateTitle.setText(currentTitle)
        dialogBinding.updateDescription.setText(currentDescription)
        dialog.show()

    }

    private fun updateNoteDatabase(noteId: String, newTitle: String, newDescription: String) {
        val currentUser = auth.currentUser
        currentUser?.let {
                user->
            val noteRefrence = databaseReference.child("users").child(user.uid).child("notes")
            val updateNote = NoteItem(newTitle,newDescription,noteId)
            noteRefrence.child(noteId).setValue(updateNote)
                .addOnCompleteListener{
                        task->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Note Updated Successfull",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this,"Failed to Update",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
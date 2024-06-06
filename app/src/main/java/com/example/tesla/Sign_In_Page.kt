package com.example.tesla

import android.app.Activity
import android.content.ContentProviderClient
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tesla.databinding.ActivitySignInPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import java.util.zip.Inflater

class Sign_In_Page : AppCompatActivity() {
    private val binding : ActivitySignInPageBinding by lazy {
        ActivitySignInPageBinding.inflate(layoutInflater)
    }
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)
        auth = Firebase.auth

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
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Sign In Failed:${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.google.setOnClickListener{
            val signInClient = googleSignInClient.signInIntent
            launcher.launch(signInClient)
        }
    }

private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
    result ->
    if (result.resultCode==Activity.RESULT_OK){
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if(task.isSuccessful){
            val account:GoogleSignInAccount? = task.result
            val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
            auth.signInWithCredential(credential).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this,"Succesfull",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                }
                else{
                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    else{
        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
    }
}

    override fun onStart() {
        super.onStart()
        val CurrentUser:FirebaseUser? = auth.currentUser
        if (CurrentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}
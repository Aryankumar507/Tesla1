package com.example.tesla

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tesla.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val addNotes:Button = findViewById(R.id.add_notes)


        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        addNotes.setOnClickListener {
            startActivity(Intent(this,AddNote::class.java))
        }

        navView.setNavigationItemSelectedListener {
           when(it.itemId){
               R.id.home->Toast.makeText(applicationContext,"home clicked",Toast.LENGTH_SHORT).show()
               R.id.Contact->Toast.makeText(applicationContext,"contact clicked",Toast.LENGTH_SHORT).show()
               R.id.open_notes->{Toast.makeText(applicationContext,"gallery clicked",Toast.LENGTH_SHORT).show()
                                 startActivity(Intent(this,AllNotes::class.java))
               }
               R.id.about->Toast.makeText(applicationContext,"about clicked",Toast.LENGTH_SHORT).show()
               R.id.login->Toast.makeText(applicationContext,"login clicked",Toast.LENGTH_SHORT).show()
               R.id.logout->{Toast.makeText(applicationContext,"logout clicked",Toast.LENGTH_SHORT).show()
                            FirebaseAuth.getInstance().signOut()
                             startActivity(Intent(this,Sign_In_Page::class.java))}

               R.id.share->Toast.makeText(applicationContext,"share clicked",Toast.LENGTH_SHORT).show()
               R.id.rate_us->Toast.makeText(applicationContext,"rate us clicked",Toast.LENGTH_SHORT).show()
           }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
package com.example.tesla

import android.os.Bundle
import android.view.MenuItem
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
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)


        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
           when(it.itemId){
               R.id.home->Toast.makeText(applicationContext,"home clicked",Toast.LENGTH_SHORT).show()
               R.id.Contact->Toast.makeText(applicationContext,"contact clicked",Toast.LENGTH_SHORT).show()
               R.id.gallery->Toast.makeText(applicationContext,"gallery clicked",Toast.LENGTH_SHORT).show()
               R.id.about->Toast.makeText(applicationContext,"about clicked",Toast.LENGTH_SHORT).show()
               R.id.login->Toast.makeText(applicationContext,"login clicked",Toast.LENGTH_SHORT).show()
               R.id.logout->Toast.makeText(applicationContext,"logout clicked",Toast.LENGTH_SHORT).show()
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
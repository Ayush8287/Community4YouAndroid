package com.example.community4you

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.community4you.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
//    private lateinit var auth: FirebaseAuth
      private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.btm_nav_layout)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

//        auth = FirebaseAuth.getInstance()
//
//        findViewById<Button>(R.id.btnLogOut).setOnClickListener{
//            auth.signOut()
//            startActivity(Intent(this,Login::class.java))
//            Toast.makeText(this,"LogOut Successfully", Toast.LENGTH_SHORT).show()
//        }
    }
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_featured -> {
                        moveToFragment(FeaturedFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_search -> {
                        moveToFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }

                R.id.nav_about -> {
                        moveToFragment(AboutFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                        moveToFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }

            false
        }

        private fun moveToFragment(fragment: Fragment) {
            val fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.replace(R.id.fl_main, fragment)
            fragmentTrans.commit()
        }
    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.fl_main)) {
            !is FeaturedFragment -> goToHome()

            else -> super.onBackPressed()
        }
    }

    private fun goToHome() {
        navView = findViewById(R.id.btm_nav_layout)
        moveToFragment(FeaturedFragment())
        navView.selectedItemId = R.id.nav_featured
    }
}
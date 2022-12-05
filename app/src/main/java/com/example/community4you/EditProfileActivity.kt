package com.example.community4you

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.community4you.fragment.ProfileFragment

class EditProfileActivity : AppCompatActivity() {

    private lateinit var cancel:ImageView
    private lateinit var success:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        cancel = findViewById(R.id.iv_cancel_ep)
        success = findViewById(R.id.iv_check_ep)


        cancel.setOnClickListener{
            startActivity(Intent(this,ProfileFragment::class.java))
        }

        success.setOnClickListener {
            startActivity(Intent(this,ProfileFragment::class.java))
        }
    }
}
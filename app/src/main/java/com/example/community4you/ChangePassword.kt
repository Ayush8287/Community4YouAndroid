package com.example.community4you

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.community4you.fragment.ProfileFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePassword : AppCompatActivity() {

    private val TAG = "MyActivity"
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        findViewById<AppCompatButton>(R.id.btn_change_password).setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val user = Firebase.auth.currentUser
        var oldPassword = findViewById<EditText>(R.id.et_old_password)
        var newPassword = findViewById<EditText>(R.id.et_new_password)
        var confirmNewPassword = findViewById<EditText>(R.id.et_confirm_new_password)
        if (oldPassword.text.toString().isNotEmpty() && newPassword.text.toString().trim{it<=' '}.isNotEmpty()&&confirmNewPassword.text.toString().trim{it<=' '}.isNotEmpty()){
            if (newPassword.text.toString() == confirmNewPassword.text.toString()){

                if (user!=null && user.email!=null){
                    val credential: AuthCredential = EmailAuthProvider.getCredential(user.email!!,oldPassword.toString())

                    user.reauthenticate(credential).addOnCompleteListener{ Log.d(TAG,"User re-authenticated")}

                    Toast.makeText(this,"Password Changed Successfully",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,ProfileFragment::class.java))
                }else{
                    startActivity(Intent(this,Login::class.java))

                }
            }else{
                Toast.makeText(this,"Password not match", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this,"Enter all the fields", Toast.LENGTH_SHORT).show()
        }
    }

}
package com.example.community4you

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.jar.Attributes.Name

class Register : AppCompatActivity() {
//    private lateinit var firstName : EditText
//    private lateinit var email : EditText
//    private lateinit var password : EditText
//    private lateinit var lastName: EditText
    private lateinit var auth:FirebaseAuth
//    private lateinit var button: Button
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val button = findViewById<Button>(R.id.btnRegister)
        auth = FirebaseAuth.getInstance()
        val firstName = findViewById<EditText?>(R.id.inputFirstName).toString()
        val lastName = findViewById<EditText>(R.id.inputLastName).toString()
        val email = findViewById<EditText>(R.id.inputEmail).toString()
        val password = findViewById<EditText>(R.id.inputPassword).toString()


        var textView = findViewById<TextView>(R.id.gotoLogin)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        button.setOnClickListener{
            when {
                TextUtils.isEmpty(firstName.toString()) -> Toast.makeText(
                    this,
                    "FullName is required.",
                    Toast.LENGTH_LONG
                ).show()
                TextUtils.isEmpty(lastName.toString()) -> Toast.makeText(
                    this,
                    "UserName is required.",
                    Toast.LENGTH_LONG
                ).show()
                TextUtils.isEmpty(email) -> Toast.makeText(
                    this,
                    "Email is required.",
                    Toast.LENGTH_LONG
                ).show()
                TextUtils.isEmpty(password) -> Toast.makeText(
                    this,
                    "Password is required.",
                    Toast.LENGTH_LONG
                ).show()

                else -> {
                    val progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("SignUp")
                    progressDialog.setMessage("Please wait, this may take a while...")
                    progressDialog.setCanceledOnTouchOutside(false)
                    progressDialog.show()

                    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                saveUserInfo(firstName, lastName, email, progressDialog)
                            } else {
                                val message = task.exception!!.toString()
                                Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                                mAuth.signOut()
                                progressDialog.dismiss()
                            }
                        }
                }
            }
        }


        textView.setOnClickListener(View.OnClickListener {

            intent = Intent(this,Login::class.java)


            startActivity(intent)
        })
    }
    private fun saveUserInfo(
        fullName: String,
        userName: String,
        email: String,
        progressDialog: ProgressDialog
    ) {
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = fullName
        userMap["smallfullname"] = fullName.toLowerCase()
        userMap["username"] = userName
        userMap["email"] = email
        userMap["bio"] = "Hey there, I am using InstagramClone."
        userMap["image"] =
            "https://firebasestorage.googleapis.com/v0/b/instagramclone-83ac2.appspot.com/o/Default_Images%2Fprofile_fill.png?alt=media&token=aaa532e4-88e0-416f-b800-aeed91ee5f4e"
        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this,
                        "Account has been created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }
    }
//    private fun checkcing():Boolean{
//        firstName = findViewById(R.id.inputFirstName)
//        lastName = findViewById(R.id.inputLastName)
//        email = findViewById(R.id.inputEmail)
//        password = findViewById(R.id.inputPassword)
//
//        if (firstName.text.toString().trim{it<=' '}.isNotEmpty() && lastName.text.toString().trim{it<=' '}.isNotEmpty() && email.text.toString().trim{it<=' '}.isNotEmpty() && password.text.toString().trim{it<=' '}.isNotEmpty()){
//            return true
//        }
//        return false
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(EXTRA_NAME, user.displayName)
            startActivity(intent)
        }
    }

    companion object {
        const val RC_SIGN_IN = 1001
        const val EXTRA_NAME = "EXTRA_NAME"
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if (result.resultCode== Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }

    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {

        if(task.isSuccessful){
            val account : GoogleSignInAccount?=task.result
            if (account!=null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful){
                val intent = Intent(this,Login::class.java)
                startActivity(intent)
                Toast.makeText(this,"Registered Successfully",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }
}

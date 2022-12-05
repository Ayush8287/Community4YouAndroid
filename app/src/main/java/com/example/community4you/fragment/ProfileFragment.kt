package com.example.community4you.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.community4you.ChangePassword
import com.example.community4you.EditProfileActivity
import com.example.community4you.Login
import com.example.community4you.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private val TAG = "MyActivity"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false) as ViewGroup

        view?.findViewById<TextView>(R.id.btn_profile)?.setOnClickListener{

            var i = Intent(container?.context ,EditProfileActivity::class.java)
            startActivity(i)
        }

        view.findViewById<ImageView>(R.id.iv_menu_pro).setOnClickListener {
            val bottomSheetDialog =
                BottomSheetDialog(container!!.context, R.style.BottomSheetDialogTheme)

            var menu = view.findViewById<RelativeLayout>(R.id.menu_dialog_container)
            var bottomSheetView = LayoutInflater.from(container.context)
                .inflate(R.layout.dialog_menu, menu)

            bottomSheetView.findViewById<RelativeLayout>(R.id.rl_log_out).setOnClickListener {
                FirebaseAuth.getInstance().signOut()

                Toast.makeText(container.context, "Logged Out!", Toast.LENGTH_SHORT).show()

                val intent = Intent(container.context, Login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                activity?.finish()
            }
            var user = Firebase.auth.currentUser!!
            bottomSheetView.findViewById<RelativeLayout>(R.id.rl_delete_account).setOnClickListener {
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "User account deleted.")
                            startActivity(Intent(container.context,Login::class.java))
                            Toast.makeText(container.context,"Account Deleted",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }


        view.findViewById<TextView>(R.id.change_password).setOnClickListener{
            startActivity(Intent(context,ChangePassword::class.java))
        }



        return view
    }


}

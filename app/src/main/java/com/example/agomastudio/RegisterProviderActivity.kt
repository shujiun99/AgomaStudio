package com.example.agomastudio

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.agomastudio.Data.Provider
import com.example.agomastudio.databinding.ActivityMainBinding
import com.example.agomastudio.databinding.ActivityRegisterProviderBinding
import com.firebase.ui.auth.AuthUI.TAG
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class RegisterProviderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterProviderBinding
    private lateinit var img : ImageView
    lateinit var imgUri: Uri
    private lateinit var auth: FirebaseAuth

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Provider")

        binding.imgProvider.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            //launchSomeActivity.launch(intent)
            startActivityForResult(intent,1000)
        }

        binding.btnRe.setOnClickListener{
            val email = binding.edUserName.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val name = binding.edtProviderName.text.toString().trim()
            val website = binding.edtWebsite.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()
            val contact = binding.edtContact.text.toString().trim()
            val objective = binding.edtObjective.text.toString().trim()

            if(email == ""){
                toast("Invalid Email")
                return@setOnClickListener
            }

            if(password == ""){
                toast("Invalid Password")
                return@setOnClickListener
            }

            if(name == ""){
                toast("Invalid name")
                return@setOnClickListener
            }

            if(website == ""){
                toast("Invalid website")
                return@setOnClickListener
            }

            if(address == ""){
                toast("Invalid Address")
                return@setOnClickListener
            }

            if(contact == ""){
                toast("Invalid Contact")
                return@setOnClickListener
            }

            if(objective == ""){
                toast("Invalid Objective")
                return@setOnClickListener
            }

            if(imgUri == null){
                toast("Invalid photo")
                return@setOnClickListener
            }

            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault() )
            val now = Date()
            val fileName = formatter.format(now)

            val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
            storageReference.putFile(imgUri).
            addOnSuccessListener {

            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                    if(task.isSuccessful){
                        Log.i("My", "signin")
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val photo = fileName.toString()
                        val id = firebaseUser.uid
                        val provider = Provider(id,name,password,website,address,contact,objective,photo,email)
                        myRef.child(id).setValue(provider)
                        onBackPressed()
                    }else{
                        Log.i("My","failed")
                        toast("Invalid Password or Email")
                    }
                })

        }

    }

    private fun updateUI(user: FirebaseUser?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            imgUri  = data?.data!!
            binding.imgProvider.setImageURI(imgUri)
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    }

}
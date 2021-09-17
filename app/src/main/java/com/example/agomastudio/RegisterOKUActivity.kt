package com.example.agomastudio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.agomastudio.Data.OKU
import com.example.agomastudio.Data.Provider
import com.example.agomastudio.databinding.ActivityRegisterOkuactivityBinding
import com.example.agomastudio.databinding.ActivityRegisterProviderBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class RegisterOKUActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterOkuactivityBinding
    private lateinit var img : ImageView
    lateinit var imgUri: Uri
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterOkuactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("OKU")

        binding.imgoku.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            //launchSomeActivity.launch(intent)
            startActivityForResult(intent,1000)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtokuemail.text.toString().trim()
            val password = binding.edtokupass.text.toString().trim()
            val name = binding.edtokuname.text.toString().trim()
            val age = binding.edtage.text.toString().trim()
            val gender = getGender()
            val address = binding.edtokuAddress.text.toString().trim()

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

            if(age == ""){
                toast("Invalid age")
                return@setOnClickListener
            }

            if(gender == ""){
                toast("Invalid gender")
                return@setOnClickListener
            }

            if(address == ""){
                toast("Invalid Address")
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
                        val oku = OKU(id,name,password,email,age,gender,address,photo)
                        myRef.child(id).setValue(oku)
                        onBackPressed()
                    }else{
                        Log.i("My","failed")
                        toast("Invalid Password or Email")
                    }
                })
        }
    }

    private fun getGender(): String {
        return when(binding.rgGender.checkedRadioButtonId){
            R.id.rgFemale -> "Female"
            R.id.rgMale -> "Male"
            else -> " "
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            imgUri  = data?.data!!
            binding.imgoku.setImageURI(imgUri)
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    }
}
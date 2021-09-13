package com.example.agomastudio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agomastudio.Data.Event
import com.example.agomastudio.Data.Provider
import com.example.agomastudio.databinding.FragmentAddEventBinding
import com.example.agomastudio.databinding.FragmentAddProviderBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class AddProviderFragment : Fragment() {

    private lateinit var binding: FragmentAddProviderBinding
    private lateinit var img : ImageView
    lateinit var imgUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProviderBinding.inflate(inflater)
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Provider")
        (activity as AppCompatActivity).supportActionBar?.title = "Register Provider"

        binding.imgProvider.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            //launchSomeActivity.launch(intent)
            startActivityForResult(intent,1000)
        }

        binding.btnRe.setOnClickListener{
            val username = binding.edUserName.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val name = binding.edtProviderName.text.toString().trim()
            val website = binding.edtWebsite.text.toString().trim()
            val address = binding.edtAddress.text.toString().trim()
            val contact = binding.edtContact.text.toString().trim()
            val objective = binding.edtObjective.text.toString().trim()

            if(username == ""){
                toast("Invalid Username")
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
            val photo = fileName.toString()
            val provider = Provider(username,name,password,website,address,contact,objective,photo)
            myRef.child(username).setValue(provider)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            imgUri  = data?.data!!
            binding.imgProvider.setImageURI(imgUri)
        }
    }

    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()
    }

    companion object {

    }
}
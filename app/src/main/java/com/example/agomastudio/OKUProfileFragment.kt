package com.example.agomastudio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.agomastudio.databinding.FragmentOKUProfileBinding
import com.example.agomastudio.databinding.FragmentProfileProviderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class OKUProfileFragment : Fragment() {
    private lateinit var binding: FragmentOKUProfileBinding
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userId = firebaseUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOKUProfileBinding.inflate(inflater)
        val dbref: DatabaseReference = FirebaseDatabase.getInstance().getReference("OKU")
        dbref.child(userId.toString()).get().addOnSuccessListener {
            if(it.exists()){
                binding.tvOKUname.text = it.child("name").value.toString()
                binding.tvOKUemail.text = it.child("email").value.toString()
                binding.tvOKUGender.text = it.child("gender").value.toString()
                binding.tvOKUage.text = it.child("age").value.toString()
                binding.tvOKUaddress.text = it.child("address").value.toString()
                val imgUri = it.child("photo").value.toString()
                val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
                Log.i("My",storageReference.toString())
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    Log.i("My", "load image")
                    Glide.with(this@OKUProfileFragment).load(uri).into(binding.imgProfile)
                }
            }
        }
        return binding.root
    }

    companion object {

    }
}
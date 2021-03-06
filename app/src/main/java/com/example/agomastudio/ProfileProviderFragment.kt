package com.example.agomastudio

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.agomastudio.databinding.FragmentAddEventBinding
import com.example.agomastudio.databinding.FragmentProfileProviderBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProfileProviderFragment : Fragment() {
    private lateinit var binding: FragmentProfileProviderBinding
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userId = firebaseUser?.uid
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileProviderBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Profile"
        val dbref: DatabaseReference = FirebaseDatabase.getInstance().getReference("Provider")
        dbref.child(userId.toString()).get().addOnSuccessListener {
            if(it.exists()){
                binding.tvproGetName.text = it.child("name").value.toString()
                binding.tvGetPuserName.text = it.child("email").value.toString()
                binding.tvGetpContact.text = it.child("contact").value.toString()
                binding.tvPGetWebsite.text = it.child("website").value.toString()
                binding.tvGetpAddress.text = it.child("address").value.toString()
                binding.tvGetPObject.text = it.child("objective").value.toString()
                val imgUri = it.child("photo").value.toString()
                val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
                Log.i("My",storageReference.toString())
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    Log.i("My", "load image")
                    Glide.with(this@ProfileProviderFragment).load(uri).into(binding.imageView)
                }
            }
        }
        return binding.root
    }

    companion object {

    }
}
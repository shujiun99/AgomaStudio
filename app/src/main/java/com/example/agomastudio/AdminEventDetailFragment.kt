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
import com.example.agomastudio.databinding.FragmentAdminEventBinding
import com.example.agomastudio.databinding.FragmentAdminEventDetailBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class AdminEventDetailFragment : Fragment() {
    private lateinit var binding: FragmentAdminEventDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminEventDetailBinding.inflate(inflater)
        val ids = arguments?.getString("id")
        val dbref: DatabaseReference
        dbref = FirebaseDatabase.getInstance().getReference("Event")
        var check:String = ""
        if(ids?.isNotEmpty()!!){
            check=ids
            dbref.child(check).get().addOnSuccessListener {
                if(it.exists()){
                    binding.tadvGetName.text = it.child("name").value.toString()
                    binding.tvGetadAddress.text = it.child("address").value.toString()
                    binding.tvadGetDescription.text = it.child("description").value.toString()
                    binding.tvadGetDate.text = it.child("date").value.toString()
                    binding.tvadGetTime.text = it.child("time").value.toString()
                    val imgUri = it.child("photo").value.toString()
                    (activity as AppCompatActivity).supportActionBar?.title = it.child("name").value.toString()
                    val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
                    Log.i("My",storageReference.toString())
                    storageReference.downloadUrl.addOnSuccessListener(object:
                        OnSuccessListener<Uri> {
                        override fun onSuccess(uri: Uri?) {
                            Log.i("My", "load image")
                            Glide.with(this@AdminEventDetailFragment).load(uri).into(binding.imgadEvent)
                        }
                    })
                    val providerid = it.child("providerId").value.toString()
                    val ref: DatabaseReference
                    ref = FirebaseDatabase.getInstance().getReference("Provider")
                    Log.i("My",providerid)
                    ref.child(providerid).get().addOnSuccessListener {
                        if(it.exists()){
                            Log.i("My","Provider Get")
                            binding.tvadNgoName.text = it.child("name").value.toString()
                        }
                    }
                    val status = it.child("status").value.toString()
                    if(status != "Pending"){
                        binding.btnApproveEv.visibility = View.GONE
                        binding.btnRejectEv.visibility = View.GONE
                    }else{
                        binding.btnupdateeventSt.visibility = View.GONE
                    }
                }
            }
        }

        binding.btnApproveEv.setOnClickListener {
            dbref.child(check).child("status").setValue("Approved")
            binding.btnRejectEv.visibility = View.GONE
            binding.btnApproveEv.visibility = View.GONE
            binding.btnupdateeventSt.visibility = View.VISIBLE
        }

        binding.btnRejectEv.setOnClickListener {
            dbref.child(check).child("status").setValue("Rejected")
            binding.btnRejectEv.visibility = View.GONE
            binding.btnApproveEv.visibility = View.GONE
            binding.btnupdateeventSt.visibility = View.VISIBLE
        }

        binding.btnupdateeventSt.setOnClickListener{
            binding.btnRejectEv.visibility = View.VISIBLE
            binding.btnApproveEv.visibility = View.VISIBLE
            binding.btnupdateeventSt.visibility = View.GONE
        }
        return binding.root
    }

    companion object {

    }
}
package com.example.agomastudio

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.agomastudio.databinding.FragmentUpdateEventBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class UpdateEventFragment : Fragment() {
    private lateinit var binding: FragmentUpdateEventBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateEventBinding.inflate(inflater)

        val name = arguments?.getString("name")
        Log.i("My",name.toString()+"Check")
        val dbref: DatabaseReference
        dbref = FirebaseDatabase.getInstance().getReference("Event")
        var check:String = ""
        Log.i("My",name.toString())
        if(name?.isNotEmpty()!!){
            check=name
            dbref.child(check).get().addOnSuccessListener {
                if(it.exists()){
                    Log.i("My","success")
                    val date = it.child("date").value
                    val time = it.child("time").value
                    val description = it.child("description").value
                    val imgUri = it.child("photo").value
                    val cate = it.child("category").value
                    val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
                    Log.i("My",storageReference.toString())
                    storageReference.downloadUrl.addOnSuccessListener(object:
                        OnSuccessListener<Uri> {
                        override fun onSuccess(uri: Uri?) {
                            Log.i("My", "load image")
                            Glide.with(this@UpdateEventFragment).load(uri).into(binding.imageEvent)
                        }
                    })
                    val eD = binding.edtDescription
                    eD.setText(description.toString())
                    val eN = binding.edName
                    eN.setText(name.toString())
                    val array = resources.getStringArray(R.array.EventCategories)
                    for(a in array){
                        var i = 0
                        if(a.toString().equals(cate)){
                            binding.spinCategory.setSelection(i)
                        }
                        else{
                            i++
                        }
                    }
                    binding.edTime.text = time.toString()
                    binding.EdDate.text = date.toString()
                }
            }
        }

        binding.btnupdate.setOnClickListener {
            val address = binding.edadd.text.toString()
            val state = binding.edState.text.toString()
            val city = binding.edCity.text.toString()
            val postal = binding.edPostal.text.toString()

        }
        return binding.root
    }

    companion object {

    }
}
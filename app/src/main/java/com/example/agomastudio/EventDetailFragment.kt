package com.example.agomastudio

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentEventDetailBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


class EventDetailFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailBinding
    private var imgUr : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventDetailBinding.inflate(inflater)
        val ids = arguments?.getString("id")
        val dbref: DatabaseReference
        dbref = FirebaseDatabase.getInstance().getReference("Event")
        var check:String = ""
        Log.i("My",id.toString())
        if(ids?.isNotEmpty()!!){
            check=ids
            dbref.child(check).get().addOnSuccessListener {
                if(it.exists()){
                    Log.i("My","success")
                    val date = it.child("date").value
                    val time = it.child("time").value
                    val description = it.child("description").value
                    val imgUri = it.child("photo").value
                    val address = it.child("address").value
                    binding.tvGetName.text = it.child("name").value.toString()
                    (activity as AppCompatActivity).supportActionBar?.title = it.child("name").value.toString()
                    imgUr = imgUri.toString()
                    val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
                    Log.i("My",storageReference.toString())
                    storageReference.downloadUrl.addOnSuccessListener(object:
                        OnSuccessListener<Uri> {
                        override fun onSuccess(uri: Uri?) {
                            Log.i("My", "load image")
                            Glide.with(this@EventDetailFragment).load(uri).into(binding.imgEvent)
                        }
                    })
                    binding.tvGetDescription.text = description.toString()
                    binding.tvGetTime.text = time.toString()
                    binding.tvGetDate.text = date.toString()
                    binding.tvGetAddress.text = address.toString()
                    if(it.child("providerId").value != "providerA"){
                        binding.btnUpdate.visibility = View.GONE
                        binding.btnDelete.visibility = View.GONE
                    }
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            var builder = AlertDialog.Builder(activity)
            builder.setTitle(getString(R.string.ConfirmDetele))
            builder.setMessage(getString(R.string.DeleteMessage))
            builder.setPositiveButton("Delete", DialogInterface.OnClickListener{dialog,id ->
                deleteData(ids)
                dialog.cancel()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            var alert: AlertDialog = builder.create()
            alert.show()
        }

        binding.btnUpdate.setOnClickListener {
            val id: String = ids
            val bundle = bundleOf(Pair("id",id))
            Navigation.findNavController(it).navigate(R.id.action_eventDetailFragment_to_updateEventFragment,bundle)
        }

        return binding.root
    }

    private fun deleteData(name: String) {
        val dbref: DatabaseReference
        dbref = FirebaseDatabase.getInstance().getReference("Event")
        dbref.child(name).removeValue().addOnSuccessListener {
            Toast.makeText(requireActivity(), "$name Deleted",Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }
        val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUr")
        Log.i("My",storageReference.toString())
        storageReference.delete().addOnSuccessListener{
                Log.i("My", "Delete image")
        }
    }

    companion object {

    }
}
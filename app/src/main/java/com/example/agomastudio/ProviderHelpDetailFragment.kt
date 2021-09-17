package com.example.agomastudio

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentProviderHelpDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class ProviderHelpDetailFragment : Fragment() {

    private lateinit var binding: FragmentProviderHelpDetailBinding
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userId = firebaseUser?.uid
    private lateinit var dbref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProviderHelpDetailBinding.inflate(inflater)
        val ids = arguments?.getString("id")

        val dbref: DatabaseReference
        dbref = FirebaseDatabase.getInstance().getReference("Help")
        var check:String = ""
        if(ids?.isNotEmpty()!!){
            check=ids
            dbref.child(check).get().addOnSuccessListener {
                if(it.exists()){
                    binding.tvHelpName.text = it.child("name").value.toString()
                    binding.tvHelpDes.text = it.child("description").value.toString()
                    (activity as AppCompatActivity).supportActionBar?.title = it.child("name").value.toString()
                    val helpst = it.child("helpStatus").value.toString()
                    if(helpst == "Pending"){
                        binding.eventHide.visibility = View.GONE
                        binding.spEvent.visibility = View.GONE
                        binding.confirmEvent.visibility = View.GONE
                        binding.btnCreateEvent.visibility = View.GONE
                        binding.tvEventHelp.visibility = View.GONE
                        binding.UpdateEvent.visibility = View.GONE
                        binding.btnEnd.visibility = View.GONE
                    }else if(helpst == "Planning"){
                        binding.btnHelp.visibility = View.GONE
                        binding.tvEventHelp.visibility = View.GONE
                        binding.UpdateEvent.visibility = View.GONE
                        binding.btnEnd.visibility = View.GONE
                    }else if(helpst == "Going"){
                        binding.tvEventHelp.text = it.child("eventName").value.toString()
                        binding.spEvent.visibility = View.GONE
                        binding.confirmEvent.visibility = View.GONE
                        binding.btnCreateEvent.visibility = View.GONE
                        binding.btnHelp.visibility = View.GONE
                    }else{
                        binding.tvEventHelp.text = it.child("eventName").value.toString()
                        binding.UpdateEvent.visibility = View.GONE
                        binding.btnEnd.visibility = View.GONE
                        binding.btnCreateEvent.visibility = View.GONE
                        binding.btnHelp.visibility = View.GONE
                        binding.spEvent.visibility = View.GONE
                        binding.confirmEvent.visibility = View.GONE
                    }
                }
            }
        }

        val s = ArrayList<String>()
        val myRef: DatabaseReference
        myRef = FirebaseDatabase.getInstance().getReference("Event")
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(Event::class.java)
                        if(event?.providerId.equals(userId)){
                            s.add(event?.name!!)
                        }
                        val adp = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,s)
                        binding.spEvent.adapter = adp
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        binding.confirmEvent.setOnClickListener {
            val event = binding.spEvent.selectedItem as String
            dbref.child(check).child("eventName").setValue(event)
            dbref.child(check).child("helpStatus").setValue("Going")
            binding.btnCreateEvent.visibility = View.GONE
            binding.confirmEvent.visibility = View.GONE
            binding.spEvent.visibility = View.GONE
            binding.tvEventHelp.text = event
            binding.tvEventHelp.visibility = View.VISIBLE
            binding.UpdateEvent.visibility = View.VISIBLE
            binding.btnEnd.visibility = View.VISIBLE
        }

        binding.btnCreateEvent.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.addEventFragment)
        }

        binding.UpdateEvent.setOnClickListener {
            binding.UpdateEvent.visibility = View.GONE
            binding.btnEnd.visibility = View.GONE
            binding.spEvent.visibility = View.VISIBLE
            binding.btnCreateEvent.visibility = View.VISIBLE
            binding.confirmEvent.visibility = View.VISIBLE
            binding.tvEventHelp.visibility = View.GONE
        }

        binding.btnEnd.setOnClickListener {
            var builder = AlertDialog.Builder(activity)
            builder.setTitle(getString(R.string.end_help))
            builder.setMessage(getString(R.string.end_message))
            builder.setPositiveButton("End", DialogInterface.OnClickListener{ dialog, id ->
                dbref.child(check).child("helpStatus").setValue("End")
                binding.btnEnd.visibility = View.GONE
                binding.UpdateEvent.visibility = View.GONE
                dialog.cancel()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            var alert: AlertDialog = builder.create()
            alert.show()
        }

        binding.btnHelp.setOnClickListener {
            dbref.child(check).child("helpStatus").setValue("Planning")
            dbref.child(check).child("providerId").setValue(userId)
            binding.btnHelp.visibility = View.GONE
            binding.eventHide.visibility = View.VISIBLE
            binding.spEvent.visibility = View.VISIBLE
            binding.btnCreateEvent.visibility = View.VISIBLE
            binding.confirmEvent.visibility = View.VISIBLE
        }
        return binding.root
    }

    companion object {

    }
}
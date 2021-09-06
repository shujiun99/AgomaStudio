package com.example.agomastudio

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentAddEventBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class AddEventFragment : Fragment(){
    private lateinit var binding : FragmentAddEventBinding
    private lateinit var img : ImageView
    lateinit var imgUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEventBinding.inflate(inflater)
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Event")

        binding.imEvent.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            //launchSomeActivity.launch(intent)
            startActivityForResult(intent,1000)
        }



        binding.btnTime.setOnClickListener(){
            val timePickerFragment = TimePickerfragement()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                    resultKey,bundle ->
                if(resultKey == "REQUEST_KEY"){
                    val date = bundle.getString("SELECTED_DATE")
                    binding.btnTime.text = date
                }
            }
            timePickerFragment.show(supportFragmentManager,"TimePickerFragment")
        }

        binding.btnDate.setOnClickListener(){
            val datePickerFragment = DatePickerfragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                resultKey,bundle ->
                if(resultKey == "REQUEST_KEY"){
                    val date = bundle.getString("SELECTED_DATE")
                    binding.btnDate.text = date
                }
            }
            datePickerFragment.show(supportFragmentManager,"DatePickerFragment")
        }

        binding.btnSubmit.setOnClickListener(){
            val name = binding.EdName.text.toString().trim()
            val date = binding.btnDate.text.toString()
            val time = binding.btnTime.text.toString()
            val description = binding.edDescription.text.toString().trim()
            val category = binding.spnCategory.selectedItem as String
            img = binding.imEvent


            if(name == ""){
                toast("Invalid name")
                return@setOnClickListener
            }

            if(date == "Select Date"){
                toast("Invalid date")
                return@setOnClickListener
            }

            if(time =="Select Time"){
                toast("Invalid time")
                return@setOnClickListener
            }

            if(description == ""){
                toast("Invalid description")
                return@setOnClickListener
            }

            if(category == ""){
                toast("Invalid category")
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

            val id = name + date
            var status = "Pending"
            var existsss = false
            //status = checkEvent(name)
            myRef.child(name).get().addOnSuccessListener{
                if(it.exists()){
                    status = "checked"
                    existsss = true
                }
                else{
                    status = "not match"
                }
            }.addOnFailureListener{ e->
                Log.i("MyTag",e.toString())
            }


            if(existsss){
                toast("The Event Exists!!")
                return@setOnClickListener
            }
            val photo = fileName.toString()
            val event = Event(name,date,time,description,category,status,photo)
            myRef.child(name).setValue(event)
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    private fun checkEvent(name: String) : String{
        var status = "unsucess"
        val database: DatabaseReference
        database = FirebaseDatabase.getInstance().getReference("Event")
        database.child(name).get().addOnSuccessListener {
            if(it.exists()){
                status = "success"
            }
        }
        return status
    }

    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text,Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            imgUri  = data?.data!!
            binding.imEvent.setImageURI(imgUri)
        }
    }

    /*var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            imgUri  = data?.data!!
            img.setImageURI(data?.data)
        }
    }*/

    companion object {

    }




}
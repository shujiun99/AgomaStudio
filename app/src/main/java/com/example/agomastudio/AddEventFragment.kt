package com.example.agomastudio

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentAddEventBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class AddEventFragment : Fragment(){
    private lateinit var binding : FragmentAddEventBinding
    private lateinit var img : ImageView
    lateinit var imgUri: Uri
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userId = firebaseUser?.uid

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEventBinding.inflate(inflater)
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Event")
        (activity as AppCompatActivity).supportActionBar?.title = "Add Event"
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
            val address = binding.tvaddress.text.toString()
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

            val datecheck = LocalDate.now()
            val list = date.split("-")
            Log.i("My",list[2] + list[1] + list[0])
            val eventDate = LocalDate.of(list[2].toInt(),list[1].toInt(),list[0].toInt())
            if(eventDate.isBefore(datecheck)){
                toast("Invalid date")
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
            var existsss = false
            myRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        Log.i("My","Database load")
                        for(eventSnapshot in snapshot.children){
                            val event = eventSnapshot.getValue(Event::class.java)
                            if(event?.id.equals(id)){
                                Log.i("My","id get")
                                existsss = true
                                Log.i("My", existsss.toString())
                            }
                        }

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.i("My","Failed to Load data")
                }
            })
            var status = "Pending"

            if(address == ""){
                toast("Invalid Address")
                return@setOnClickListener
            }

            var longitude = ""
            var latitude = ""

            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
            Log.i("My","ready get geo")
            val addList = geoCoder.getFromLocationName(address,1)
            Log.i("My","done get geo")
            try{
                if(addList != null && addList.size > 0){
                    val add = addList.get(0) as Address
                    Log.i("My",add.latitude.toString() + " " + add.longitude.toString())
                    longitude = add.longitude.toString()
                    latitude = add.latitude.toString()
                }
            }catch(e: IOException){
                Log.e("My","Unable to connect to GeoCoder",e)
            }
            if(longitude == "" || latitude == ""){
                toast("Invalid Address")
                return@setOnClickListener
            }

            if(existsss){
                toast("The Event Exists!!")
                return@setOnClickListener
            }else{
                val photo = fileName.toString()
                val event = Event(id,name,date,time,description,category,status,photo,longitude,latitude,address,userId.toString())
                myRef.child(id).setValue(event)
                requireActivity().onBackPressed()
            }
        }
        return binding.root
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

    companion object {

    }




}
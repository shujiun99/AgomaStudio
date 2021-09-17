package com.example.agomastudio

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentUpdateEventBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UpdateEventFragment : Fragment() {
    private lateinit var binding: FragmentUpdateEventBinding
    private var imgUr : String = ""
    private lateinit var img : ImageView
    lateinit var imgUriu: Uri
    private var status: String = ""
    private var id: String = ""
    private var pic = false
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userId = firebaseUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateEventBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Update Event"
        val name = arguments?.getString("id")
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
                    val address = it.child("address").value
                    val names = it.child("name").value
                    status = it.child("status").value.toString()
                    id = it.child("id").value.toString()
                    imgUr = imgUri.toString()
                    val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
                    Log.i("My",storageReference.toString())
                    storageReference.downloadUrl.addOnSuccessListener(object:
                        OnSuccessListener<Uri> {
                        override fun onSuccess(uri: Uri?) {
                            Log.i("My", "load image")
                            imgUriu = uri!!
                            Glide.with(this@UpdateEventFragment).load(uri).into(binding.imageEvent)
                        }
                    })
                    val eD = binding.edtDescription
                    eD.setText(description.toString())
                    val eN = binding.edName
                    eN.setText(names.toString())
                    val eA = binding.edadd
                    eA.setText(address.toString())

                    CoroutineScope(Main).launch {
                        val array = resources.getStringArray(R.array.EventCategories)
                        delay(1000)
                        for(a in array){
                            var i = 0
                            if(a.toString().equals(cate)){
                                binding.spinCategory.setSelection(i)
                            }
                            else{
                                i++
                            }
                        }
                    }

                    binding.edTime.text = time.toString()
                    binding.EdDate.text = date.toString()
                }
            }
        }

        binding.edTime.setOnClickListener(){
            val timePickerFragment = TimePickerfragement()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                    resultKey,bundle ->
                if(resultKey == "REQUEST_KEY"){
                    val date = bundle.getString("SELECTED_DATE")
                    binding.edTime.text = date
                }
            }
            timePickerFragment.show(supportFragmentManager,"TimePickerFragment")
        }

        binding.EdDate.setOnClickListener(){
            val datePickerFragment = DatePickerfragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                    resultKey,bundle ->
                if(resultKey == "REQUEST_KEY"){
                    val date = bundle.getString("SELECTED_DATE")
                    binding.EdDate.text = date
                }
            }
            datePickerFragment.show(supportFragmentManager,"DatePickerFragment")
        }

        binding.imageEvent.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            //launchSomeActivity.launch(intent)
            startActivityForResult(intent,1000)
        }

        binding.btnupdate.setOnClickListener {
            val address = binding.edadd.text.toString()
            val name = binding.edName.text.toString().trim()
            val date = binding.EdDate.text.toString()
            val time = binding.edTime.text.toString()
            val description = binding.edtDescription.text.toString().trim()
            val category = binding.spinCategory.selectedItem as String

            img = binding.imageEvent
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("Event")

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

            if(address == ""){
                toast("Invalid Address")
                return@setOnClickListener
            }

            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault() )
            val now = Date()
            val fileName = formatter.format(now)
            val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUr")
            Log.i("My",storageReference.toString())
            storageReference.delete().addOnSuccessListener{
                Log.i("My", "Delete image")
            }
            val storageReferencew = FirebaseStorage.getInstance().getReference("images/$fileName")
            storageReferencew.putFile(imgUriu).
            addOnSuccessListener {
                Log.i("My","pic true")
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
            val photo = fileName.toString()
            val event = Event(id,name,date,time,description,category,status,photo,longitude,latitude,address,userId.toString())
            myRef.child(id).setValue(event)
            requireActivity().onBackPressed()

        }
        return binding.root
    }

    private fun toast(text: String) {
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            imgUriu  = data?.data!!
            binding.imageEvent.setImageURI(imgUriu)
            pic = true
        }
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }
        return 0
    }

    companion object {

    }
}
package com.example.agomastudio

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.agomastudio.Data.OKU
import com.example.agomastudio.Data.Provider
import com.example.agomastudio.databinding.ActivityMainBinding
import com.example.agomastudio.databinding.ActivityProviderBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ProviderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProviderBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }
    private lateinit var abc : AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(R.id.providerEventFragment, R.id.providerHelpFragment,R.id.profileProviderFragment,R.id.providerServiceFragment),
            binding.drawerLayout
        )

        setupActionBarWithNavController(nav, abc)
        binding.navView.setupWithNavController(nav)

        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("Provider")
        val userId = firebaseUser?.uid
        if (userId != null) {
            myRef.child(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val provider = snapshot.getValue(Provider::class.java)
                    val header = binding.navView.getHeaderView(0)
                    val imgUri = provider?.photo
                    val photo = header.findViewById<ImageView>(R.id.photo)
                    val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
                    Log.i("My",storageReference.toString())
                    storageReference.downloadUrl.addOnSuccessListener(object:
                        OnSuccessListener<Uri> {
                        override fun onSuccess(uri: Uri?) {
                            Log.i("My", "load image")
                            Glide.with(this@ProviderActivity).load(uri).into(photo)
                        }
                    })
                    header.findViewById<TextView>(R.id.name).text = provider?.name
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
    override fun onSupportNavigateUp():Boolean{
        return nav.navigateUp(abc) || super.onSupportNavigateUp()
    }
}
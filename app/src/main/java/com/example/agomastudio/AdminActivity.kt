package com.example.agomastudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agomastudio.databinding.ActivityAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.hostadmin)!!.findNavController() }
    private lateinit var abc : AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(R.id.adminEventFragment, R.id.adminHelpFragment,R.id.adminServiceFragment),
            binding.drawerAdminLayout
        )

        setupActionBarWithNavController(nav, abc)
        binding.navAdminView.setupWithNavController(nav)
        val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        //Access to navigation view's header
        val header = binding.navAdminView.getHeaderView(0)
        header.findViewById<ImageView>(R.id.photo).setImageResource(R.drawable.admin)
        header.findViewById<TextView>(R.id.name).text = "Admin"
    }

    override fun onSupportNavigateUp():Boolean{
        return nav.navigateUp(abc) || super.onSupportNavigateUp()
    }
}
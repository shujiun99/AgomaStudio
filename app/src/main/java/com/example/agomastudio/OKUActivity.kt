package com.example.agomastudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agomastudio.databinding.ActivityOkuactivityBinding

class OKUActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOkuactivityBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.hostoku)!!.findNavController() }
    private lateinit var abc : AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOkuactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(R.id.OKUEventFragment),
            binding.drawerOKULayout
        )

        setupActionBarWithNavController(nav, abc)
        binding.navOKUView.setupWithNavController(nav)

        //Access to navigation view's header
        //val header = binding.navView.getHeaderView(0)
        //header.findViewById<ImageView>(R.id.photo).setImageResource(R.drawable.hdni)
        //header.findViewById<TextView>(R.id.name).text = "dhf"
    }
    override fun onSupportNavigateUp():Boolean{
        return nav.navigateUp(abc) || super.onSupportNavigateUp()
    }
}
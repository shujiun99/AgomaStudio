package com.example.agomastudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agomastudio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }

    private lateinit var abc : AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        abc = AppBarConfiguration(
            setOf(R.id.providerEventFragment, R.id.providerHelpFragment),
            binding.drawerLayout
        )

        setupActionBarWithNavController(nav, abc)
        binding.navView.setupWithNavController(nav)

        //Access to navigation view's header
        //val header = binding.navView.getHeaderView(0)
        //header.findViewById<ImageView>(R.id.photo).setImageResource(R.drawable.hdni)
        //header.findViewById<TextView>(R.id.name).text = "dhf"
    }

    override fun onSupportNavigateUp():Boolean{
        return nav.navigateUp(abc) || super.onSupportNavigateUp()
    }
}
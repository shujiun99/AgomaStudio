package com.example.agomastudio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agomastudio.databinding.ActivityMainBinding
import com.example.agomastudio.databinding.ActivityProviderBinding

class ProviderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProviderBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }
    private lateinit var abc : AppBarConfiguration
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
    }
    override fun onSupportNavigateUp():Boolean{
        return nav.navigateUp(abc) || super.onSupportNavigateUp()
    }
}
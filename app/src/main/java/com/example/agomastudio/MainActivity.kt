package com.example.agomastudio

import android.content.Intent
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

    private lateinit var abc : AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheck.setOnClickListener{
            val intent: Intent = Intent(this, ProviderActivity::class.java)
            startActivity(intent)
        }

        binding.btnnnn.setOnClickListener {
            val intent: Intent = Intent(this, OKUActivity::class.java)
            startActivity(intent)
        }

        binding.button22.setOnClickListener {
            val intent: Intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
    }


}
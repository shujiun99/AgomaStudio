package com.example.agomastudio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agomastudio.Data.Event
import com.example.agomastudio.Data.Provider
import com.example.agomastudio.databinding.FragmentAddEventBinding
import com.example.agomastudio.databinding.FragmentAddProviderBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class AddProviderFragment : Fragment() {

    private lateinit var binding: FragmentAddProviderBinding
    private lateinit var img : ImageView
    lateinit var imgUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProviderBinding.inflate(inflater)

        return binding.root
    }



    companion object {

    }
}
package com.example.agomastudio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.agomastudio.databinding.FragmentAddEventBinding
import com.example.agomastudio.databinding.FragmentProfileProviderBinding

class ProfileProviderFragment : Fragment() {
    private lateinit var binding: FragmentProfileProviderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileProviderBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Profile"

        return binding.root
    }

    companion object {

    }
}
package com.example.agomastudio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.agomastudio.databinding.FragmentAdminEventBinding


class AdminEventFragment : Fragment() {
    private lateinit var binding: FragmentAdminEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminEventBinding.inflate(inflater)

        return binding.root
    }

    companion object {

    }
}
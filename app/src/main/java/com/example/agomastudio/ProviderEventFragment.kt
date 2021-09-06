package com.example.agomastudio

import android.icu.text.CaseMap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentProviderEventBinding
import com.google.firebase.database.*


class ProviderEventFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Event>
    private lateinit var viewModel : ProviderEventViewModel
    private lateinit var dbref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProviderEventBinding.inflate(inflater)

        viewModel = ViewModelProvider(this).get(ProviderEventViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.navigateToSearch.observe(viewLifecycleOwner,
        Observer<Boolean>{ navigate ->
            if(navigate){
                val navController = findNavController()
                navController.navigate(R.id.action_providerEventFragment_to_addEventFragment)
                viewModel.onNavigatedToSearch()
            }
        })
        
        newRecyclerView = binding.recyclerView
        newRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Event>()
        getEventData()


        return binding.root
    }

    private fun getEventData() {
        dbref = FirebaseDatabase.getInstance().getReference("Event")


        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    newArrayList.clear()
                    Log.i("My","come in")
                    for(eventSnapshot in snapshot.children){
                        val event = eventSnapshot.getValue(Event::class.java)
                        newArrayList.add(event!!)
                    }
                    newRecyclerView.adapter = EventAdapter(newArrayList)
                    Log.i("My", newArrayList.size.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


       /* var adapter = EventAdapter(newArrayList)
        newRecyclerView.adapter = adapter

        adapter.setOnClickListener(object: EventAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                *//* viewModel.navigateToSearch.observe(viewLifecycleOwner,
                     Observer<Boolean>{ navigate ->
                         if(navigate){
                             val navController = findNavController()
                             navController.navigate(R.id.action_providerEventFragment_to_eventDetailFragment)
                             viewModel.onNavigatedToSearch()
                         }
                     })*//*

                //Navigation.findNavController(it).navigate(R.id.action_providerEventFragment_to_eventDetailFragment)
            }

        })*/
    }

    companion object {
        fun newInstance() = ProviderEventFragment()
    }
}
package com.example.agomastudio

import android.icu.text.CaseMap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentProviderEventBinding
import com.google.firebase.database.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProviderEventFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Event>
    private lateinit var viewModel : ProviderEventViewModel
    private lateinit var dbref: DatabaseReference
    private val nav by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProviderEventBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Event"
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

        binding.filter.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val cate = binding.spCategory.selectedItem as String
                val me = binding.spMy.selectedItem as String
                val date = LocalDate.now()
                dbref = FirebaseDatabase.getInstance().getReference("Event")
                dbref.addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            newArrayList.clear()
                            Log.i("My","come in filter")
                            for(eventSnapshot in snapshot.children){
                                val event = eventSnapshot.getValue(Event::class.java)
                                val list = event?.date!!.split("-")
                                Log.i("My",list[2] + list[1] + list[0])
                                val eventDate = LocalDate.of(list[2].toInt(),list[1].toInt(),list[0].toInt())
                                if(cate == event?.category){
                                    if(me == "All"){
                                        if(date.isBefore(eventDate)){
                                            newArrayList.add(event!!)
                                        }
                                    }
                                    else{
                                        if(event?.providerId == "providerA"){
                                            if(eventDate.isBefore(date)){
                                                newArrayList.add(event!!)
                                            }
                                        }
                                    }
                                }
                                if(cate == "All"){
                                    if(date.isBefore(eventDate)) {
                                        newArrayList.add(event!!)
                                    }
                                }
                            }
                            newRecyclerView.adapter = EventAdapter(newArrayList)
                            Log.i("My", newArrayList.size.toString())
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

        })

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
                        val date = LocalDate.now()
                        val list = event?.date!!.split("-")
                        Log.i("My",list[2] + list[1] + list[0])
                        val eventDate = LocalDate.of(list[2].toInt(),list[1].toInt(),list[0].toInt())
                        if(date.isBefore(eventDate)){
                            newArrayList.add(event!!)
                        }
                    }
                    newRecyclerView.adapter = EventAdapter(newArrayList)
                    Log.i("My", newArrayList.size.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        Log.i("My", newArrayList.size.toString()+"Test")

    }

    companion object {
        fun newInstance() = ProviderEventFragment()
    }
}
package com.example.agomastudio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentProviderHistoryBinding
import com.google.firebase.database.*
import java.time.LocalDate


class ProviderHistoryFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Event>
    private lateinit var binding: FragmentProviderHistoryBinding
    private lateinit var dbref: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProviderHistoryBinding.inflate(inflater)
        newRecyclerView = binding.rvHistory
        newRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Event>()
        getEventData()

        binding.ClickSearch.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val status = binding.spinner.selectedItem as String
                dbref = FirebaseDatabase.getInstance().getReference("Event")
                dbref.addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            newArrayList.clear()
                            Log.i("My","come in filter")
                            for(eventSnapshot in snapshot.children){
                                val event = eventSnapshot.getValue(Event::class.java)
                                if(event?.status == status){
                                    if(event.providerId == "providerA"){
                                        newArrayList.add(event!!)
                                    }
                                }
                                if(status == "All"){
                                    if(event?.providerId == "providerA"){
                                        newArrayList.add(event!!)
                                    }
                                }
                            }
                            newRecyclerView.adapter = EventAdapter(newArrayList)
                            Log.i("My", newArrayList.size.toString())
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
            }
        })
        return binding.root
    }

    private fun getEventData() {
        dbref = FirebaseDatabase.getInstance().getReference("Event")
        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    newArrayList.clear()
                    Log.i("My","come in")
                    for(eventSnapshot in snapshot.children){
                        val event = eventSnapshot.getValue(Event::class.java)
                        if(event?.providerId == "providerA"){
                            newArrayList.add(event!!)
                        }
                    }
                    newRecyclerView.adapter = EventAdapter(newArrayList)
                    Log.i("My", newArrayList.size.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        Log.i("My", newArrayList.size.toString()+"Test")
    }

    companion object {

    }
}
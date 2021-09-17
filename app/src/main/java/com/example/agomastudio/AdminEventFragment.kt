package com.example.agomastudio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agomastudio.Data.Event
import com.example.agomastudio.databinding.FragmentAdminEventBinding
import com.google.firebase.database.*
import java.time.LocalDate


class AdminEventFragment : Fragment() {
    private lateinit var binding: FragmentAdminEventBinding
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Event>
    private lateinit var dbref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminEventBinding.inflate(inflater)
        newRecyclerView = binding.rvadminEvent
        newRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Event>()
        getEventData("Pending")

        binding.clickEventPending.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                getEventData("Pending")
            }
        })

        binding.clickApprovedEvent.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                getEventData("Approved")
            }
        })

        binding.clickRejectedEvent.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                getEventData("Rejected")
            }
        })

        return binding.root
    }

    private fun getEventData(status: String) {
        dbref = FirebaseDatabase.getInstance().getReference("Event")
        dbref.addValueEventListener(object: ValueEventListener {
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
                            if(event.status == status){
                                newArrayList.add(event!!)
                            }
                        }
                    }
                    newRecyclerView.adapter = AdminEventAdapter(newArrayList)
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
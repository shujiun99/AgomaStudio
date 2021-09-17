package com.example.agomastudio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agomastudio.Data.EventJoin
import com.example.agomastudio.Data.OKU
import com.example.agomastudio.databinding.FragmentProviderEventOListBinding
import com.google.firebase.database.*


class ProviderEventOListFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<OKU>
    private lateinit var binding: FragmentProviderEventOListBinding
    private lateinit var dbref: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProviderEventOListBinding.inflate(inflater)
        val ids = arguments?.getString("id")
        Log.i("My",ids.toString())
        newRecyclerView = binding.rvOKUlist
        newRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<OKU>()
        getOKUData(ids)
        return binding.root
    }

    private fun getOKUData(ids: String?) {
        dbref = FirebaseDatabase.getInstance().getReference("EventJoin")
        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    newArrayList.clear()
                    for(joinSnapshot in snapshot.children){
                        val okuJo = joinSnapshot.getValue(EventJoin::class.java)
                        val okuId = okuJo?.okuId
                        if(okuJo?.eventId.equals(ids)){
                            Log.i("My",okuJo?.eventId.toString())
                            val myRef = FirebaseDatabase.getInstance().getReference("OKU")
                            myRef.child(okuId!!).get().addOnSuccessListener {
                                if(it.exists()){
                                    val oku = OKU(
                                        it.child("id").value as String,
                                        it.child("name").value as String,
                                        it.child("password").value as String,
                                        it.child("email").value as String,
                                        it.child("age").value as String,
                                        it.child("gender").value as String,
                                        it.child("address").value as String,
                                        it.child("photo").value as String
                                    )
                                    newArrayList.add(oku)
                                    binding.tvRVcount.text = newArrayList.size.toString()
                                }
                            }
                        }
                    }
                    newRecyclerView.adapter = OKUAdapter(newArrayList)
                    Log.i("My",newArrayList.size.toString())

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    companion object {

    }
}
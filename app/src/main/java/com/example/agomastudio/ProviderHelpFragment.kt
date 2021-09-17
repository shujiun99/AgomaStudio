package com.example.agomastudio

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agomastudio.Data.Help
import com.example.agomastudio.Data.OKU
import com.example.agomastudio.databinding.FragmentProviderHelpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class ProviderHelpFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Help>
    private lateinit var dbref: DatabaseReference
    private lateinit var binding: FragmentProviderHelpBinding
    val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userId = firebaseUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProviderHelpBinding.inflate(inflater)

        newRecyclerView = binding.rvHelp
        newRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Help>()
        getHelpData()

       binding.clickHelpHis.setOnClickListener(object: View.OnClickListener{
           override fun onClick(v: View?) {
               dbref = FirebaseDatabase.getInstance().getReference("Help")
               dbref.addValueEventListener(object: ValueEventListener{
                   override fun onDataChange(snapshot: DataSnapshot) {
                       if(snapshot.exists()){
                           newArrayList.clear()
                           for(helpSnapshot in snapshot.children){
                               val help = helpSnapshot.getValue(Help::class.java)
                               if(help?.providerId == userId){
                                   newArrayList.add(help!!)
                               }
                           }
                           newRecyclerView.adapter = HelpAdapter(newArrayList)
                       }
                   }
                   override fun onCancelled(error: DatabaseError) {
                       Log.i("My","Failed to get Help")
                   }

               })
           }
       })


        binding.clickHelpList.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                getHelpData()
            }
        })


        return binding.root
    }

    private fun getHelpData() {
        dbref = FirebaseDatabase.getInstance().getReference("Help")
        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    newArrayList.clear()
                    for(helpSnapshot in snapshot.children){
                        val help = helpSnapshot.getValue(Help::class.java)
                        if(help?.status == "Approved"){
                            if(help.helpStatus == "Pending"){
                                newArrayList.add(help!!)
                            }
                        }
                    }
                    newRecyclerView.adapter = HelpAdapter(newArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("My","Failed to get Help")
            }

        })
    }

    companion object {

    }
}
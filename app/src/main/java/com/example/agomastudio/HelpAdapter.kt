package com.example.agomastudio

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agomastudio.Data.Help
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class HelpAdapter(private val helpList: ArrayList<Help>):
    RecyclerView.Adapter<HelpAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpAdapter.MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.help_item, parent,false)
        return HelpAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HelpAdapter.MyViewHolder, position: Int) {
        val item = helpList[position]
        holder.help.text = item.name
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("OKU")
        myRef.child(item.okuId).get().addOnSuccessListener {
            if(it.exists()){
                holder.okuName.text = it.child("name").value.toString()
                val imgUri = it.child("photo").value.toString()
                val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
                Log.i("My",storageReference.toString())
                storageReference.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri> {
                    override fun onSuccess(uri: Uri?) {
                        Log.i("My","load")
                        Glide.with(holder.photo.context).load(uri).into(holder.photo)
                    }
                })
            }
        }
        holder.itemView.setOnClickListener(){
            val id: String = item.id
            val bundle = bundleOf(Pair("id",id))
            Navigation.findNavController(it).navigate(R.id.providerHelpDetailFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return helpList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val root = itemView
        val photo : ImageView = itemView.findViewById(R.id.imageHelp)
        val help : TextView = itemView.findViewById(R.id.tvHelp)
        val okuName : TextView = itemView.findViewById(R.id.tvHelpOku)

    }

}
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
import com.example.agomastudio.Data.Event
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage

class AdminEventAdapter (private val eventList:ArrayList<Event>):
    RecyclerView.Adapter<AdminEventAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminEventAdapter.MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.admin_event, parent,false)
        return AdminEventAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminEventAdapter.MyViewHolder, position: Int) {
        val item = eventList[position]
        holder.name.text = item.name
        holder.date.text = item.date
        Log.i("My", item.name)
        var imgUri = item.photo
        Log.i("My", "meme" + item.photo)
        val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
        Log.i("My",storageReference.toString())
        storageReference.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri> {
            override fun onSuccess(uri: Uri?) {
                Log.i("My","load")
                Glide.with(holder.photo.context).load(uri).into(holder.photo)
            }
        })
        holder.itemView.setOnClickListener(){
            val id: String = item.id
            val bundle = bundleOf(Pair("id",id))
            Navigation.findNavController(it).navigate(R.id.adminEventDetailFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val root = itemView
        val photo : ImageView = itemView.findViewById(R.id.imgEventadmin)
        val name : TextView = itemView.findViewById(R.id.eventadminName)
        val date : TextView = itemView.findViewById(R.id.tvadmineventDate)

    }
}
package com.example.agomastudio

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agomastudio.Data.Event
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage

class TestAdapter : ListAdapter<Event, TestAdapter.MyViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Event>(){
        override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.equals(newItem)
        override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestAdapter.MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.event_item, parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)
        //val item = eventList[position]
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
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val root = itemView
        val photo : ImageView = itemView.findViewById(R.id.imgPhoto)
        val name : TextView = itemView.findViewById(R.id.txtAuthor)
        val date : TextView = itemView.findViewById(R.id.tvShowDate)

    }
}
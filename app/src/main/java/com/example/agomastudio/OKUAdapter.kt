package com.example.agomastudio

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agomastudio.Data.OKU
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage

class OKUAdapter(private val okuList: ArrayList<OKU>):
    RecyclerView.Adapter<OKUAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val root = itemView
        val photo : ImageView = itemView.findViewById(R.id.imgPOKU)
        val name : TextView = itemView.findViewById(R.id.txtOKUName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.okulist_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = okuList[position]
        holder.name.text = item.name
        val imgUri = item.photo
        val storageReference = FirebaseStorage.getInstance().reference.child("images/$imgUri")
        Log.i("My",storageReference.toString())
        storageReference.downloadUrl.addOnSuccessListener(object: OnSuccessListener<Uri> {
            override fun onSuccess(uri: Uri?) {
                Log.i("My","load")
                Glide.with(holder.photo.context).load(uri).into(holder.photo)
            }
        })
    }

    override fun getItemCount(): Int {
        return okuList.size
    }
}
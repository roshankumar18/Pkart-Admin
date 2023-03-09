package com.example.pkartadmin.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pkartadmin.R
import com.example.pkartadmin.databinding.ImgItemLayoutBinding

class AddProductImageAdapter(var context : Context , val list : ArrayList<Uri>) : RecyclerView.Adapter<AddProductImageAdapter.AddProductViewHolder>() {

    class AddProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ImgItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.img_item_layout,parent,false)
        return AddProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddProductViewHolder, position: Int) {
        holder.binding.itemImg.setImageURI(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
package com.example.pkartadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pkartadmin.R
import com.example.pkartadmin.databinding.ItemCategoryLayoutBinding
import com.example.pkartadmin.model.CategoryModel

class CategoryAdapter(var context : Context, val list:ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = ItemCategoryLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_category_layout,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(context).load(list[position].img).into(holder.binding.imageView3)
        holder.binding.textView2.text = list[position].cate
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
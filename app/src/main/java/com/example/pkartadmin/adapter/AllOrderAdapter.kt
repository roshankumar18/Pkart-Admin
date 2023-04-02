package com.example.pkartadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pkartadmin.R
import com.example.pkartadmin.databinding.AllOrderItemLayoutBinding
import com.example.pkartadmin.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AllOrderAdapter(var context: Context,var list: ArrayList<AllOrderModel>) : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {
    class AllOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = AllOrderItemLayoutBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.all_order_item_layout,parent,false)
        return AllOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productTitle.setText(list[position].name)
        holder.binding.productPrice.setText(list[position].sp)
        when(list[position].status){
            "Ordered"->{
                holder.binding.proceedButton.text = "Ordered"
                holder.binding.cancelButton.setOnClickListener {
                    updateStatus("Cancelled",list[position].orderId)
                }
                holder.binding.proceedButton.setOnClickListener {
                    updateStatus("Dispatched",list[position].orderId)
                }
            }
            "Dispatched"->{
                holder.binding.proceedButton.text = "Dispatched"
                holder.binding.cancelButton.setOnClickListener {
                    updateStatus("Cancelled",list[position].orderId)
                }
                holder.binding.proceedButton.setOnClickListener {
                    updateStatus("Delivered",list[position].orderId)
                }
            }
            "Delivered"->{
                holder.binding.cancelButton.visibility = View.GONE
                holder.binding.proceedButton.text = "Delivered"
                holder.binding.proceedButton.setOnClickListener {
                    holder.binding.proceedButton.text = "Already Delivered"
                }


            }
            "Cancelled"->{
                holder.binding.proceedButton.visibility = View.GONE
                holder.binding.cancelButton.text = "Cancelled"
            }
        }
    }

    private fun updateStatus(status: String, orderId: String?) {
        val data = hashMapOf<String,Any>()
        data["status"]=status
        Firebase.firestore.collection("allOrders").document(orderId!!)
            .update(data).addOnSuccessListener {
                Toast.makeText(context, "Updated status", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
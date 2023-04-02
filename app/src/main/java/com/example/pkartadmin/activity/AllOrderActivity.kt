package com.example.pkartadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import com.example.pkartadmin.R
import com.example.pkartadmin.adapter.AllOrderAdapter
import com.example.pkartadmin.databinding.ActivityAllOrderBinding
import com.example.pkartadmin.model.AllOrderModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AllOrderActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAllOrderBinding
    private val TAG = "AllOrderActivity"
    private lateinit var adapter: AllOrderAdapter
    private lateinit var list: ArrayList<AllOrderModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Firebase.firestore.collection("allOrders").get().addOnSuccessListener {
            list = ArrayList()
            for (data in it){
                list.add(data.toObject())
            }
            Log.w(TAG, "onCreate: $list", )
            adapter = AllOrderAdapter(this,list)
            binding.orderRecycler.adapter = adapter
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}
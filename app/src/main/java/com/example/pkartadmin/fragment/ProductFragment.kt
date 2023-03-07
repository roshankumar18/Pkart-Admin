package com.example.pkartadmin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pkartadmin.R
import com.example.pkartadmin.databinding.FragmentHomeBinding
import com.example.pkartadmin.databinding.FragmentProductBinding


class ProductFragment : Fragment() {
    private lateinit var binding:FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(layoutInflater)
        binding.FloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_addProductFragment)
        }
        return binding.root
    }


}
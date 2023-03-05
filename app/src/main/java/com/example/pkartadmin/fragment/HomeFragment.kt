package com.example.pkartadmin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pkartadmin.R
import com.example.pkartadmin.activity.AllOrderActivity
import com.example.pkartadmin.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }
        binding.button1.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
        }
        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment)
        }
        binding.button3.setOnClickListener {
            startActivity(Intent(requireContext(),AllOrderActivity::class.java))
        }
        return binding.root
    }

}
package com.example.pkartadmin.fragment

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pkartadmin.R
import com.example.pkartadmin.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID


class SliderFragment : Fragment() {

    private lateinit var binding : FragmentSliderBinding
    private lateinit var dialog : Dialog
    private var imageURI : Uri? = null
    private val launchGalleryActivity = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        Toast.makeText(context, "inside content", Toast.LENGTH_SHORT).show()
        imageURI = uri
        binding.imageView.setImageURI(uri)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSliderBinding.inflate(layoutInflater)

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.apply {
            imageView.setOnClickListener {
                launchGalleryActivity.launch("image/*")
            }
            button.setOnClickListener {
                if (imageURI!=null){
                    uploadImage(imageURI!!)
                }else{
                    Toast.makeText(context, "Please select image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun uploadImage(uri: Uri) {
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = Firebase.storage.reference.child("slider/$fileName")
        val uploadTask = storageRef.putFile(uri)
            .addOnSuccessListener {
            Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            it.storage.downloadUrl.addOnSuccessListener {imageUrl->
                storeData(imageUrl.toString())
            }
            dialog.dismiss()
        }.addOnFailureListener{
            Toast.makeText(context, "Not able to upload", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    private fun storeData(downloadUri: String) {
        val db = Firebase.firestore
        val data = hashMapOf<String , Any>(
            "img" to downloadUri
        )
        db.collection("slider").document("item").set(data)
            .addOnSuccessListener {
                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show()
            }
    }


}
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pkartadmin.R
import com.example.pkartadmin.adapter.CategoryAdapter
import com.example.pkartadmin.databinding.FragmentCategoryBinding
import com.example.pkartadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log


class CategoryFragment : Fragment() {
    private val TAG : String = "CategoryFragment"
    private lateinit var binding:FragmentCategoryBinding
    private lateinit var adapter: CategoryAdapter
    private lateinit var dialog : Dialog
    private var imageURI : Uri? = null
    private val launchGalleryActivity = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

        imageURI = uri
        binding.imageView2.setImageURI(uri)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)
        getData()

        binding.apply {
            binding.imageView2.setOnClickListener {
                launchGalleryActivity.launch("image/*")
            }
            uploadButton.setOnClickListener{
               validateData(categoryName.text.toString())
            }
        }
        return binding.root
    }
private fun getData(){
    val list = ArrayList<CategoryModel>()
    Firebase.firestore.collection("category")
        .get()
        .addOnSuccessListener {
//            list.clear()
           for (document in it.documents){

               val data = document.toObject<CategoryModel>()
               list.add(data!!)

           }

            binding.categoryRecycler.adapter = CategoryAdapter(requireContext(),list)
        }


}


    private fun validateData(name: String) {
        if(name.isEmpty()){
            Toast.makeText(context, "Please enter text", Toast.LENGTH_SHORT).show()

        }else if (imageURI==null){
            Toast.makeText(context, "Please select image", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage(name,imageURI!!)
        }
    }
    private fun uploadImage(name: String,uri: Uri) {
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = Firebase.storage.reference.child("category/$fileName")
        val uploadTask = storageRef.putFile(uri)
            .addOnSuccessListener {
                Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                it.storage.downloadUrl.addOnSuccessListener {imageUrl->
                    storeData(name,imageUrl.toString())
                }
                binding.imageView2.setImageDrawable(resources.getDrawable(R.drawable.imageprev))
                binding.categoryName.text = null
                dialog.dismiss()
            }.addOnFailureListener{
                Toast.makeText(context, "Not able to upload", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
    }
    private fun storeData(name: String, downloadUri: String) {
        val db = Firebase.firestore
        val data = hashMapOf<String , Any>(
            "cate" to name,
            "img" to downloadUri
        )
        db.collection("category").add(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Category Added", Toast.LENGTH_SHORT).show()
//                getData()
            }.addOnFailureListener{
                Toast.makeText(context, "failure", Toast.LENGTH_SHORT).show()
            }
    }

}
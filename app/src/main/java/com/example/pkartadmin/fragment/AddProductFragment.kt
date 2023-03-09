package com.example.pkartadmin.fragment

import android.app.Dialog
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pkartadmin.R
import com.example.pkartadmin.adapter.AddProductImageAdapter
import com.example.pkartadmin.databinding.FragmentAddProductBinding
import com.example.pkartadmin.model.AddProductModel
import com.example.pkartadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList


class AddProductFragment : Fragment() {
    private lateinit var binding:FragmentAddProductBinding
    private lateinit var adapter : AddProductImageAdapter
    private lateinit var dialog:Dialog
    private  var coverImage: Uri? = null
    private lateinit var list :ArrayList<Uri>
    private lateinit var categoryList:ArrayList<String>
    private lateinit var imagesList:ArrayList<String>
    private lateinit var coverImageUrl:String

    private val launchGalleryActivity = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

        coverImage = uri
        binding.imageView.setImageURI(uri)
        binding.imageView.visibility  = View.VISIBLE
    }

    private val launchProductActivity = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->


        list.add(uri!!)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.selectCoverImg.setOnClickListener{
            launchGalleryActivity.launch("image/*")
        }

        binding.productSelectBtn.setOnClickListener{
            launchProductActivity.launch("image/*")
        }
        list = ArrayList()
        imagesList =ArrayList()
        adapter = AddProductImageAdapter(requireContext(),list)
        binding.productImgRecyclerview.adapter = adapter

        binding.submitProductBtn.setOnClickListener {
            validateData()
        }

        setProductCategory()
        return binding.root
    }

    private fun validateData() {
        if (binding.productNameEdit.text.toString().isEmpty()){
            binding.productNameEdit.requestFocus()
            binding.productNameEdit.error = "Empty"
        }else  if (binding.productDescEdit.text.toString().isEmpty()){
            binding.productDescEdit.requestFocus()
            binding.productDescEdit.error = "Empty"
        }
        else  if (binding.productSpEdit.text.toString().isEmpty()){
            binding.productSpEdit.requestFocus()
            binding.productSpEdit.error = "Empty"
        }
        else  if (binding.productMrpEdit.text.toString().isEmpty()){
            binding.productMrpEdit.requestFocus()
            binding.productMrpEdit.error = "Empty"
        }else if(coverImage==null){
            Toast.makeText(requireContext(), "Please select cover image", Toast.LENGTH_SHORT).show()
        }else if (list.size<1){
            Toast.makeText(requireContext(), "Please select atleast one image", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()

        }
    }

    private fun uploadImage() {
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = Firebase.storage.reference.child("products/$fileName")
        val uploadTask = storageRef.putFile(coverImage!!)
            .addOnSuccessListener {
                Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                it.storage.downloadUrl.addOnSuccessListener {imageUrl->
                     coverImageUrl = imageUrl.toString()
                    uploadProductImage()
                }
                binding.imageView.visibility = View.GONE
            }.addOnFailureListener{
                Toast.makeText(context, "Not able to upload", Toast.LENGTH_SHORT).show()

            }
    }
    private var i = 0
    private fun uploadProductImage() {
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = Firebase.storage.reference.child("products/$fileName")
        val uploadTask = storageRef.putFile(list[i])
            .addOnSuccessListener {
                Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                it.storage.downloadUrl.addOnSuccessListener {imageUrl->
                    imagesList.add(imageUrl.toString())
                   if (list.size == imagesList.size){
                       storeData()
                   }else{
                       i += 1
                       uploadProductImage()
                   }
                }
                binding.imageView.visibility = View.INVISIBLE

                dialog.dismiss()
            }.addOnFailureListener{
                Toast.makeText(context, "Not able to upload", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
    }

    private fun storeData() {
        val db = Firebase.firestore.collection("products")
        val key = db.document().id
        val data = AddProductModel(
            binding.productNameEdit.text.toString(),
            binding.productDescEdit.text.toString(),
            coverImageUrl,
            categoryList[binding.productCategoryDropdown.selectedItemPosition],
            key,
            binding.productMrpEdit.text.toString(),
            binding.productSpEdit.text.toString(),
            imagesList
        )
        db.document(key).set(data).addOnSuccessListener {
            Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
            binding.productNameEdit.text = null
            binding.productSpEdit.text = null
            binding.productMrpEdit.text = null
            binding.productDescEdit.text = null
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Error $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setProductCategory(){
        categoryList = ArrayList()
        Firebase.firestore.collection("category").get()
            .addOnSuccessListener {
                categoryList.clear()
                for (doc in it.documents){
                    val data = doc.toObject<CategoryModel>()
                    categoryList.add(data!!.cate!!)
                }
                categoryList.add(0,"Select Category")
                val arrayAdapter =  ArrayAdapter(requireContext(),R.layout.dropdown_item_layout,categoryList)
                binding.productCategoryDropdown.adapter = arrayAdapter
            }
    }


}
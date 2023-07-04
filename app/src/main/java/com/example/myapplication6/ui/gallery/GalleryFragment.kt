package com.example.myapplication6.ui.gallery


import com.example.myapplication6.MyApplication
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.google.gson.Gson
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication6.databinding.FragmentGalleryBinding
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private var imageUrlList: ArrayList<Image> = arrayListOf()
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        // DB 불러오기
        val gsonString = MyApplication.prefs.getString("data", "")

        imageUrlList = gsonString?.let { gsonToArray(it) }!!

        //어댑터 연결
        binding.recyclerView.adapter = GalleryAdapter(requireContext(), imageUrlList)
        val adapter = binding.recyclerView.adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context,3)

        // 이미지 저장.
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK) {
                var imageUri = it.data?.data
                //Toast.makeText(requireContext(), imageUrl.toString(), Toast.LENGTH_SHORT).show()

                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver , imageUri)
                imageUri = saveImageToInternalStorage(bitmap)
                val newImg = Image(imageUri.toString(),imageUri.toString())
                imageUrlList.add(newImg)
                val jsonString = arrayToGson(imageUrlList)
                MyApplication.prefs.setString("data", jsonString)

                adapter?.notifyItemInserted(imageUrlList.size -1)
            }
        }
        //갤러리에서 이미지 로드하기
        binding.buttonLoadImage.setOnClickListener{
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            getResult.launch(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(context)
        val long_now = System.currentTimeMillis()
        val imageName = "image"+long_now.toString()+".jpg"
        Log.d("imagename", imageName)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, imageName)

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(file)
    }
    fun arrayToGson(arrayList: ArrayList<Image>):String{
        val gson = Gson()
        val jsonString = gson.toJson(arrayList)
        println(jsonString)
        return jsonString
    }
    //이미지로드
    fun gsonToArray(jsonString:String):ArrayList<Image>{
        if(jsonString==""){
            return arrayListOf<Image>()
        }
        val gson = Gson()
        val arrayListType = object : TypeToken<ArrayList<Image>>() {}.type
        val imageList: ArrayList<Image> = gson.fromJson(jsonString, arrayListType)
        for(image in imageList){
            println("name: ${image.name}, age : ${image.url}")
        }
        return imageList
    }
}
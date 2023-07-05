package com.example.myapplication6.ui.address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication6.MyApplication
import com.example.myapplication6.databinding.FragmentAddressBinding
import com.example.myapplication6.ui.matching.Characteristics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //val profileList = ArrayList<Profile>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(AddressViewModel::class.java)

        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        var profileList = ArrayList<Characteristics>()
        val address_json_string = requireActivity().assets.open("address_list.json").reader().readText()
        val jsonString = MyApplication.prefs.getString("addressData",address_json_string)
        profileList = jsonString.let { gsonToArray(it) }



        //val jsonString = requireActivity().assets.open("simpledata.json").reader().readText()
        Log.d("JSON STR", jsonString)

        /* 2. JSONArray 로 파싱
        val jsonArray = JSONArray(jsonString)
        Log.d("jsonArray", jsonArray.toString())

        // 3. JSONArray 순회: 인덱스별 JsonObject 취득후, key에 해당하는 value 확인
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)

            val img=jsonObject.getString("img")
            val id = jsonObject.getString("id")
            val language = jsonObject.getString("language")

            Log.d("jsonObject", jsonObject.toString())
            Log.d("json_id_language", "$id $language")

            profileList.add(Profile(img, id, language, "add"))
        }

         */

        val adapter = AddressAdapter(requireContext(), profileList)
        adapter.setOnCancelClickListener { position ->
            profileList.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyItemRangeChanged(position, profileList.size)
            val jsonString = arrayToGson(profileList)
            MyApplication.prefs.setString("addressData", jsonString)
            val bundle = Bundle()
            bundle.putString("profileList", jsonString)
            this.setFragmentResult("delete", bundle)
        }




        binding.rv.adapter = adapter //AddressAdapter(requireContext(), profileList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        return root
    }
    fun arrayToGson(arrayList: ArrayList<Characteristics>):String{
        val gson = Gson()
        val jsonString = gson.toJson(arrayList)
        println(jsonString)
        return jsonString
    }
    //이미지로드
    fun gsonToArray(jsonString:String):ArrayList<Characteristics>{
        if(jsonString==""){
            return arrayListOf<Characteristics>()
        }
        val gson = Gson()
        val arrayListType = object : TypeToken<ArrayList<Characteristics>>() {}.type
        val profileList: ArrayList<Characteristics> = gson.fromJson(jsonString, arrayListType)
        for(profile in profileList){
            println("img: ${profile.img}, name : ${profile.name}, age : ${profile.age}, phone : ${profile.phone}, male : ${profile.male}, kaist : ${profile.kaist}")
        }
        return profileList
    }
    fun readJSONFromFile(context: Context, resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return jsonString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
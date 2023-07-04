package com.example.myapplication6.ui.address

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication6.MyApplication
import com.example.myapplication6.databinding.FragmentAddressBinding
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
        lateinit var getResult: ActivityResultLauncher<Intent>

        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        val root: View = binding.root



        var profileList = ArrayList<Profile>()
        val address_json_string = requireActivity().assets.open("address_list.json").reader().readText()
        val jsonString = MyApplication.prefs.getString("addressData",address_json_string)
        profileList = jsonString.let { gsonToArray(it) }

        //val jsonString = requireActivity().assets.open("simpledata.json").reader().readText()
        Log.d("JSON STR", jsonString)


        val adapter = AddressAdapter( requireContext(), profileList)
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
        adapter.setOnItemClickListener{ position ->
            profileList[position] = showDialog(position,profileList[position],profileList,adapter )
            adapter.notifyDataSetChanged()
            val jsonString = arrayToGson(profileList)
            MyApplication.prefs.setString("addressData", jsonString)
            val bundle = Bundle()
            bundle.putString("profileList", jsonString)
            this.setFragmentResult("update", bundle)
        }

        binding.rv.adapter = adapter //AddressAdapter(requireContext(), profileList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        return root
    }
    private fun showDialog(position: Int, profile: Profile, profileList: ArrayList<Profile>,
                           adapter: AddressAdapter): Profile {

        val dialog  = ProfileDialog(requireContext(),profile, position, profileList, adapter){ modifiedProfile ->
            profileList[position] = modifiedProfile
            adapter.notifyDataSetChanged()
            val jsonString = arrayToGson(profileList)
            MyApplication.prefs.setString("addressData", jsonString)
            val bundle = Bundle()
            bundle.putString("profileList", jsonString)
            this.setFragmentResult("update", bundle)
        }

        dialog.show()
        return dialog.profile
    }


    fun arrayToGson(arrayList: ArrayList<Profile>):String{
        val gson = Gson()
        val jsonString = gson.toJson(arrayList)
        println(jsonString)
        return jsonString
    }
    //이미지로드
    fun gsonToArray(jsonString:String):ArrayList<Profile>{
        if(jsonString==""){
            return arrayListOf<Profile>()
        }
        val gson = Gson()
        val arrayListType = object : TypeToken<ArrayList<Profile>>() {}.type
        val profileList: ArrayList<Profile> = gson.fromJson(jsonString, arrayListType)
        for(profile in profileList){
            println("img: ${profile.img}, name : ${profile.name}, phone: ${profile.phone}")
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
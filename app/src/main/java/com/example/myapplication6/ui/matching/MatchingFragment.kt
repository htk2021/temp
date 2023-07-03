package com.example.myapplication6.ui.matching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication6.MyApplication
import com.example.myapplication6.databinding.FragmentMatchingBinding
import com.example.myapplication6.ui.address.Profile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections.shuffle

class MatchingFragment : Fragment() {

    private var _binding: FragmentMatchingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(MatchingViewModel::class.java)
        _binding = FragmentMatchingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        var profileList = ArrayList<Profile>()
        val address_json_string = requireActivity().assets.open("address_list.json").reader().readText()
        val jsonString = MyApplication.prefs.getString("addressData", address_json_string)

        profileList = jsonString.let { gsonToArray(it) }
       /*
        this.setFragmentResultListener("delete", { key, bundle ->
            val result = bundle.getString("profileList")
            profileList = gsonToArray(result)
        })*/
        /*
        profileList.add(Profile("1조", "김병훈", "강은비", ""))
        profileList.add(Profile("2조", "박성빈", "박윤배", ""))
        profileList.add(Profile("3조", "남지현", "황태경", ""))
        profileList.add(Profile("1조", "김병훈", "강은비", ""))
        profileList.add(Profile("2조", "박성빈", "박윤배", ""))
        profileList.add(Profile("3조", "남지현", "황태경", ""))
        profileList.add(Profile("4조", "3인조", "3인조", "3인조"))
        */

        val student_list : ArrayList<String> = ArrayList(profileList.map { it.name })

        var teamList= ArrayList<Team>()
        shuffle(student_list)
        for(i: Int in 0..14 step(2)){
            teamList.add(Team((i/2+1).toString()+"조", student_list[i], student_list[i+1], ""))
        }
        teamList.add(Team("9조", student_list[16], student_list[17], student_list[18]))

        binding.rv.adapter = MatchingAdapter(requireContext(), teamList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        binding.showContentButton.setOnClickListener {
            binding.textNotifications.visibility = View.VISIBLE
            binding.rv.visibility = View.VISIBLE
            binding.showContentButton.visibility=View.INVISIBLE
        }

        return root
    }

    // 프로필 리스트를 ARRAY로 변환
    fun gsonToArray(jsonString:String):ArrayList<Profile>{
        if(jsonString==""){
            return arrayListOf<Profile>(
                    )
        }
        val gson = Gson()
        val arrayListType = object : TypeToken<ArrayList<Profile>>() {}.type
        val profileList: ArrayList<Profile> = gson.fromJson(jsonString, arrayListType)
        for(profile in profileList){
            println("img: ${profile.img}, name : ${profile.name}, age : ${profile.age}, additionalInfo : ${profile.additionalInfo}")
        }
        return profileList
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
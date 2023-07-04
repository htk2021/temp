package com.example.myapplication6.ui.matching

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.example.myapplication6.MyApplication
import com.example.myapplication6.R
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

        val student_list : ArrayList<String> = ArrayList(profileList.map { it.name })

        var teamList= ArrayList<Team>()
        shuffle(student_list)

        if(student_list.size%2==0){ //인원수가 짝수인 경우
            for(i: Int in 0..student_list.size-1 step(2)){
                teamList.add(Team((i/2+1).toString()+"조", student_list[i], student_list[i+1], ""))
            }
        }
        else{ //인원수가 홀수인 경우
            for(i: Int in 0..student_list.size-4 step(2)){
                teamList.add(Team((i/2+1).toString()+"조", student_list[i], student_list[i+1], ""))
            }
            teamList.add(Team(((student_list.size-3)/2+1).toString()+"조", student_list[student_list.size-3], student_list[student_list.size-2], student_list[student_list.size-1]))
        }

        //gif 로딩
        Glide.with(this).asGif().load(R.drawable.loading).into(binding.gifImage)

        binding.rv.adapter = MatchingAdapter(requireContext(), teamList)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        binding.showContentButton.setOnClickListener {
            binding.textNotifications.visibility = View.VISIBLE
            binding.gifImage.visibility = View.VISIBLE
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
            println("img: ${profile.img}, name : ${profile.name}, age : ${profile.phone}")
        }
        return profileList
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
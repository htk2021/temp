package com.example.myapplication6.ui.matching

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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

        var profileList = ArrayList<Characteristics>()
        val address_json_string = requireActivity().assets.open("address_list.json").reader().readText()
        val jsonString = MyApplication.prefs.getString("addressData", address_json_string)

        profileList = jsonString.let { gsonToArray(it) }

      
      

        //greedy
        val student_list : ArrayList<Characteristics> = ArrayList(profileList)
        shuffle(student_list)
        var teamList= ArrayList<Team>()

        if(student_list.size%2==0){ //인원수가 짝수인 경우
            var a: Int = 0
            val ori_size=student_list.size/2
            while(a<ori_size){
                var min_idx=1
                var min_temp=100 //무한대 역할
                for(i: Int in 1..student_list.size-1){
                    if(count_similarity(student_list[0],student_list[i])==0){
                        min_idx=i
                        min_temp=0
                        break
                    }
                    else if(count_similarity(student_list[0],student_list[i])<min_temp){
                        min_idx=i
                        min_temp=count_similarity(student_list[0],student_list[i])
                    }
                }
                a++
                teamList.add(Team(a.toString()+"조", student_list[0].name, student_list[min_idx].name, "", student_list[0].img, student_list[min_idx].img, ""))

                student_list.removeAt(min_idx)
                student_list.removeAt(0)
            }
        }
        else{ //인원수가 홀수인 경우
            var a: Int = 0
            val ori_size=(student_list.size-3)/2
            while(a<ori_size){
                var min_idx=1
                var min_temp=100 //무한대 역할
                for(i: Int in 1..student_list.size-1){
                    var fortest=count_similarity(student_list[0],student_list[i])
                    if(count_similarity(student_list[0],student_list[i])==0){
                        min_idx=i
                        min_temp=0
                        break
                    }
                    else if(count_similarity(student_list[0],student_list[i])<min_temp){
                        min_idx=i
                        min_temp=count_similarity(student_list[0],student_list[i])
                    }
                }
                a++
                teamList.add(Team(a.toString()+"조", student_list[0].name, student_list[min_idx].name, "", student_list[0].img, student_list[min_idx].img, ""))

                student_list.removeAt(min_idx)
                student_list.removeAt(0)
            }
            teamList.add(Team((a+1).toString()+"조", student_list[0].name, student_list[1].name, student_list[2].name, student_list[0].img, student_list[1].img, student_list[2].img))
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

    fun count_similarity(A:Characteristics, B:Characteristics):Int{
        return (if (A.male == B.male) 2 else 0) + (if (A.age == B.age) 1 else 0) + (if (A.kaist == B.kaist) 2 else 0)
    }

    // 프로필 리스트를 ARRAY로 변환
    fun gsonToArray(jsonString:String):ArrayList<Characteristics>{
        if(jsonString==""){
            return arrayListOf<Characteristics>()
        }
        val gson = Gson()
        val arrayListType = object : TypeToken<ArrayList<Characteristics>>() {}.type
        val profileList: ArrayList<Characteristics> = gson.fromJson(jsonString, arrayListType)
        for(profile in profileList){


            println("img: ${profile.img}, name : ${profile.name}, age : ${profile.age}, phone : ${profile.phone}, kaist : ${profile.kaist}, male : ${profile.male}")

        }
        return profileList
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}